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

//        viewModel.addPlace(DBPlaceDTO(0.0, 0.0, "aboba", "cool place"))

        binding.placesRecyclerView.layoutManager = LinearLayoutManager(context)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                viewModel.deletePlace(
//                    (binding.placesRecyclerView.adapter as PlacesListAdapter).data[position]
//                )
            }
        }).attachToRecyclerView(binding.placesRecyclerView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDBEvents().observe(viewLifecycleOwner) {
            binding.placesRecyclerView.adapter = EventListAdapter(it!!, this)
        }

    }

    override fun onItemClick(position: Int) {
//        val uid = (((binding.placesRecyclerView.adapter as EventListAdapter).data[position]) as DBPlaceDTO).uid
//        val action = PlacesListFragmentDirections.actionPlacesFragmentToPlacesInfoFragment2(uid)
//        findNavController().navigate(action)
    }


}