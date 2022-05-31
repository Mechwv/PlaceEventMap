package com.mechwv.placeeventmap

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import com.mechwv.placeeventmap.databinding.LayoutFullScreenDialogBinding
import com.mechwv.placeeventmap.databinding.MapFragmentBinding

class FullScreenDialog : DialogFragment() {
    private lateinit var binding: LayoutFullScreenDialogBinding
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
        binding = LayoutFullScreenDialogBinding.inflate(layoutInflater, container, false)
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