package com.example.placeeventmap.presentation.places

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.placeeventmap.databinding.FragmentPlacesInfoBinding
import com.example.placeeventmap.presentation.retrofit.Common
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.lang.Exception

@AndroidEntryPoint
class PlacesInfoFragment : Fragment() {

    private lateinit var binding: FragmentPlacesInfoBinding
    private val viewModel: PlacesInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacesInfoBinding.inflate(layoutInflater, container, false)

        binding.watch.setOnClickListener {
            val uid = arguments?.get("place_id") as Int
            val action = PlacesInfoFragmentDirections.actionPlacesInfoFragment2ToEventAddFragment(uid)
            findNavController().navigate(action)
        }

        binding.share.setOnClickListener {
            val sendIntent = Intent()
            val uid = arguments?.get("place_id") as Int
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "https://placeevent.map/places/$uid"
            )
            sendIntent.type = "text/plain"
            val shareIntent: Intent = Intent.createChooser(sendIntent, null)
            binding.root.context.startActivity(shareIntent)
        }

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPlace(arguments?.get("place_id") as Int).observe(viewLifecycleOwner, { place ->
        Toast.makeText(context, "${place.latitude},${place.longtitude}", Toast.LENGTH_SHORT).show()
            viewModel.getAddress(place).observe(viewLifecycleOwner, {
                binding.address.text = it
            })
            binding.placeLat.setText(place.latitude.toString())
            binding.placeLong.setText(place.longtitude.toString())

        })
    }
}