package com.example.placeeventmap.presentation.places

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.placeeventmap.databinding.PlacesListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesListFragment : Fragment() {

    private lateinit var binding: PlacesListFragmentBinding

    companion object {
        fun newInstance() = PlacesListFragment()
    }

    private val viewModel: PlacesViewModel by viewModels()

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
//                viewModel.deletePatternInfo(
//                    (binding.patternsRecyclerView.adapter as PatternsListAdapter).data[position]
//                )
            }
        }).attachToRecyclerView(binding.placesRecyclerView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDBPlaces().observe(viewLifecycleOwner, {
            binding.placesRecyclerView.adapter = PlacesListAdapter(it!!)
        })

    }


}