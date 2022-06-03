package com.mechwv.placeeventmap.presentation.events

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mechwv.placeeventmap.databinding.EventListFragmentBinding
import com.mechwv.placeeventmap.databinding.PlacesListFragmentBinding
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.places.PlacesListAdapter
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import com.mechwv.placeeventmap.presentation.room.dto.DBPlaceDTO
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class EventListFragment : Fragment(), EventListAdapter.onItemClickListener {

    private lateinit var binding: EventListFragmentBinding

    private var eventId: Long? = null


    companion object {
        fun newInstance() = EventListFragment()
    }

    private val viewModel: EventListViewModel by viewModels()

    private var activityResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allAreGranted = true
            for(b in result.values) {
                allAreGranted = allAreGranted && b
            }
            if(allAreGranted) {
                eventId?.let { CalendarHandler.deleteEvent(it, requireContext()) }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EventListFragmentBinding.inflate(layoutInflater, container, false)

        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(context)

        val searchEdit = binding.searchContainer
        searchEdit.setEndIconOnClickListener {
            updatePlacesInfo(binding.searchText.text.toString())
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val evt = (binding.eventsRecyclerView.adapter as EventListAdapter).data[position]
                viewModel.deleteEvent(evt)
                eventId = evt.calendarEventId
                val appPerms = arrayOf(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR,
                )
                activityResultLauncher.launch(appPerms)

            }
        }).attachToRecyclerView(binding.eventsRecyclerView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updatePlacesInfo()
    }

    override fun onItemClick(position: Int) {
        val uid = (((binding.eventsRecyclerView.adapter as EventListAdapter).data[position]) as DBEventDTO).uid
        val action = EventListFragmentDirections.actionEventsFragmentToEventsInfoFragment(uid)
        findNavController().navigate(action)
    }

    fun updatePlacesInfo(filter: String = "") {
        viewModel.getDBEvents(filter).observe(viewLifecycleOwner) {
            binding.eventsRecyclerView.adapter = EventListAdapter(it!!, this)
        }
    }


}