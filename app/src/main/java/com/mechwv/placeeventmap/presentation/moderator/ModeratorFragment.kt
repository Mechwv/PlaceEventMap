package com.mechwv.placeeventmap.presentation.moderator

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mechwv.placeeventmap.R
import com.mechwv.placeeventmap.databinding.FragmentPlacesInfoBinding
import com.mechwv.placeeventmap.databinding.ModeratorFragmentBinding
import com.mechwv.placeeventmap.presentation.places.PlacesInfoViewModel
import com.mechwv.placeeventmap.presentation.places.PlacesListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModeratorFragment : Fragment(), PlacesListAdapter.onItemClickListener {

    private lateinit var binding: ModeratorFragmentBinding
    private val viewModel: ModeratorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ModeratorFragmentBinding.inflate(layoutInflater, container, false)
        binding.placesRecyclerView.layoutManager = LinearLayoutManager(context)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jwtToken = arguments?.get("jwtToken") as String
        viewModel.getModeratorPlaces(jwtToken).observe(viewLifecycleOwner, {
//            Log.e("VIEW PLACES", it.toString())
            binding.placesRecyclerView.adapter = PlacesListAdapter(it!!, this)
        })

    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }


}