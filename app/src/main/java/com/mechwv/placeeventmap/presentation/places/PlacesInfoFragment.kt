package com.mechwv.placeeventmap.presentation.places

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mechwv.placeeventmap.databinding.FragmentPlacesInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

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

        binding.watchOnMap.setOnClickListener {
            val uid = arguments?.get("place_id") as Int
            val action = PlacesInfoFragmentDirections.actionPlacesInfoFragment2ToMapFragment(uid)
            findNavController().navigate(action)
        }

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPlace(arguments?.get("place_id") as Int).observe(viewLifecycleOwner) { place ->
//        Toast.makeText(context, "${place.latitude},${place.longtitude}", Toast.LENGTH_SHORT).show()
            viewModel.getAddress(place).observe(viewLifecycleOwner) {
                if (it != "")
                    binding.address.text = it
                else {
                    Toast.makeText(context, "Sorry, the place is unavailable", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.placeName.setText(place.name)
            binding.placeLat.setText(place.latitude.toString())
            binding.placeLong.setText(place.longtitude.toString())

        }
    }
}