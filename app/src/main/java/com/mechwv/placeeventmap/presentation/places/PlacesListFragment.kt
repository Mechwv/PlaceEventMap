package com.mechwv.placeeventmap.presentation.places

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
class PlacesListFragment : Fragment(), PlacesListAdapter.onItemClickListener {

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
                val position = viewHolder.adapterPosition
                viewModel.deletePlace(
                    (binding.placesRecyclerView.adapter as PlacesListAdapter).data[position]
                )
            }
        }).attachToRecyclerView(binding.placesRecyclerView)

        binding.placesAddButton.setOnClickListener {
            viewModel.addPlace(Place(
                0,
                String.format("%.7f",Random.nextDouble(-90.0, 90.0)).replace(",", ".").toDouble(),
                String.format("%.7f",Random.nextDouble(-180.0, 180.0)).replace(",", ".").toDouble(),
                "CringePlace + ${Random.nextInt(1000)}",
                "I have bought + ${Random.nextInt(1000)} bottles of pepsi there",
            null))
        }

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
        val uid = (((binding.placesRecyclerView.adapter as PlacesListAdapter).data[position]) as DBPlaceDTO).uid
        val action = PlacesListFragmentDirections.actionPlacesFragmentToPlacesInfoFragment2(uid)
        findNavController().navigate(action)
    }

    fun updatePlacesInfo(filter: String = "") {
        viewModel.getDBPlaces(filter).observe(viewLifecycleOwner) {
            binding.placesRecyclerView.adapter = PlacesListAdapter(it!!, this)
        }
    }

}