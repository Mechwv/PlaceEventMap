package com.mechwv.placeeventmap.presentation.events

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mechwv.placeeventmap.databinding.EventInfoFragmentBinding
import com.mechwv.placeeventmap.domain.model.Event
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class EventsInfoFragment : Fragment() {

    private lateinit var binding: EventInfoFragmentBinding
    private val viewModel: EventsViewModel by viewModels()

    private var placeId: Int? = null
    val calendar = Calendar.getInstance()
    private var updEvent: Event? = null
    private var eventId: Long? = null
    var temp: LocalDateTime? = null


    private var activityResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allAreGranted = true
            for(b in result.values) {
                allAreGranted = allAreGranted && b
            }
            if(allAreGranted) {
                if ((updEvent != null) && (eventId != null)) {
                    CalendarHandler.updateEvent(
                        calendar =  calendar,
                        title = updEvent!!.name,
                        "Адрес: ${binding.placeNameText.text}\nОписание: ${binding.descText.text}",
                        eventID = eventId!!,
                        requireContext()
                    )
                }
//                updEvent = null
            }
        }

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

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                temp = LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
                binding.dateText.text = temp?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
            }

        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                temp = LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
                binding.timeText.text = temp?.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
            }

        binding.pickTime.setOnClickListener {

            TimePickerDialog(requireContext(), timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show()

            DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
            temp = LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())

            binding.dateText.text =  temp?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
            binding.timeText.text =  temp?.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
        }

        binding.watch.setOnClickListener {
            eventId?.let { e -> goToCalendarIntent(e) }
        }

        binding.update.setOnClickListener {
            val uid = arguments?.get("event_id") as Long
            val fullFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            if (temp != null) {
                val e = Event(
                    id = uid,
                    description = binding.descText.text.toString(),
                    name = binding.eventNameText.text.toString(),
                    startTime = temp!!.format(fullFormat),
                )
                viewModel.updateEvent(e)
                updEvent = e

                val appPerms = arrayOf(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR,
                )
                activityResultLauncher.launch(appPerms)
                Toast.makeText(context, "Событие успешно обновлено", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Выберите время", Toast.LENGTH_SHORT).show()
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
            val tmp = LocalDateTime.parse(event.startTime, fullFormat)
            binding.dateText.text = tmp.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()
            binding.timeText.text = tmp.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
            binding.eventNameText.setText(event.name)
            binding.descText.setText(event.description)
            placeId = event.locationId
            eventId = event.calendarEventId
            temp = tmp
        }
    }

    private fun goToCalendarIntent(eventID: Long){
        val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID)
        val intent = Intent(Intent.ACTION_VIEW).setData(uri)
        startActivity(intent)
    }
}