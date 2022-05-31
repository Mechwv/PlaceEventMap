package com.mechwv.placeeventmap.presentation.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.mechwv.placeeventmap.R
import com.mechwv.placeeventmap.databinding.PlaceCreateDialogBinding
import com.mechwv.placeeventmap.domain.model.Place
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceCreateDialog : DialogFragment() {
    private lateinit var binding: PlaceCreateDialogBinding
    private val viewModel: PlaceCreateViewModel by viewModels()
    var longitude: Double = 1.1
    var latitude: Double = 2.2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1.
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
        if (arguments != null) {
            val mArgs = requireArguments()
            longitude = mArgs.getDouble("longitude")
            latitude = mArgs.getDouble("latitude")
        }
    }

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlaceCreateDialogBinding.inflate(layoutInflater, container, false)
        //TODO databinding
        binding.button.setOnClickListener {
            val place = Place(
                latitude = binding.latitudeText.text.toString().toDouble(),
                longitude = binding.longitudeText.text.toString().toDouble(),
                name = binding.placeText.text.toString(),
                description = binding.descText.text.toString()
            )
            viewModel.addPlace(place)
            dismiss()
        }
        // Inflate the layout to use as dialog or embedded fragment
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // 3.
        Log.d("FRAGMENT", "$longitude + $latitude")
        binding.latitudeText.setText(latitude.toString())
        binding.longitudeText.setText(longitude.toString())

        requireDialog().window?.setWindowAnimations(
            R.style.DialogAnimation
        )
    }
}