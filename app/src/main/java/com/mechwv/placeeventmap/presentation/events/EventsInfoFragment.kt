package com.mechwv.placeeventmap.presentation.events

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mechwv.placeeventmap.databinding.EventInfoFragmentBinding
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class EventsInfoFragment : Fragment() {

    private lateinit var binding: EventInfoFragmentBinding
    private val viewModel: EventsViewModel by viewModels()

    private var placeId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EventInfoFragmentBinding.inflate(layoutInflater, container, false)

        binding.toPlace.setOnClickListener {
            Log.d("PLACEID", placeId.toString())
            val action = placeId?.let { it1 ->
                EventsInfoFragmentDirections.actionEventsInfoFragmentToPlacesInfoFragment(
                    it1
                )
            }
            if (action != null) {
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, "У этого события нет места", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getEvent(arguments?.get("event_id") as Long).observe(viewLifecycleOwner) { event ->
            binding.placeNameText.text = event.placeName
            binding.eventNameText.setText(event.name)
            val fullFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val temp = LocalDateTime.parse(event.startTime, fullFormat)
            binding.dateText.text = temp.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
            binding.timeText.text = temp.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
            binding.eventNameText.setText(event.name)
            binding.descText.setText(event.description)
            placeId = event.locationId
        }
    }
}