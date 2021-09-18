package com.example.placeeventmap.presentation.places

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.placeeventmap.R
import com.example.placeeventmap.databinding.FragmentPlacesInfoBinding


class PlacesInfoFragment : Fragment() {

    private lateinit var binding: FragmentPlacesInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlacesInfoBinding.inflate(layoutInflater, container, false)

        val view = binding.root

        binding.textView1.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_placesInfoFragment_to_placesFragment)
            Log.e("click", "PlacesInfoFragment")
        }

        return view
    }


}