package com.mechwv.placeeventmap.presentation.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mechwv.placeeventmap.databinding.PlacesListFragmentBinding
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.places.PlacesListAdapter
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import com.mechwv.placeeventmap.presentation.room.dto.DBPlaceDTO
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class EventListFragment : Fragment(), EventListAdapter.onItemClickListener {

    private lateinit var binding: PlacesListFragmentBinding

    companion object {
        fun newInstance() = EventListFragment()
    }

    private val viewModel: EventListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlacesListFragmentBinding.inflate(layoutInflater, container, false)

        binding.placesRecyclerView.layoutManager = LinearLayoutManager(context)

        val searchEdit = binding.searchContainer
        searchEdit.setEndIconOnClickListener {
            updatePlacesInfo(binding.searchText.text.toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updatePlacesInfo()
    }

    override fun onItemClick(position: Int) {
        val uid = (((binding.placesRecyclerView.adapter as EventListAdapter).data[position]) as DBEventDTO).uid
        val action = EventListFragmentDirections.actionEventsFragmentToEventsInfoFragment(uid)
        findNavController().navigate(action)
    }

    fun updatePlacesInfo(filter: String = "") {
        viewModel.getDBEvents(filter).observe(viewLifecycleOwner) {
            binding.placesRecyclerView.adapter = EventListAdapter(it!!, this)
        }
    }


}