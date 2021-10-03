package com.example.placeeventmap.presentation.events

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.CalendarCache.TIMEZONE_TYPE_AUTO
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.placeeventmap.databinding.AddEventFragmentBinding
import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.places.PlacesListFragment
import com.example.placeeventmap.presentation.room.dto.DBPlaceDTO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

@AndroidEntryPoint
class EventAddFragment: Fragment() {
    private val viewModel: EventAddViewModel by viewModels()
    private lateinit var binding: AddEventFragmentBinding

    var temp: LocalDateTime? = null
    val calendar = Calendar.getInstance()
    var eventId: Long? = null

    private var activityResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { result ->
            var allAreGranted = true
            for(b in result.values) {
                allAreGranted = allAreGranted && b
            }
            if(allAreGranted) {
                eventId = CalendarHandler.useCalendar(calendar, requireContext())
                viewModel.updatePlace(arguments?.get("place_id") as Int, eventId!!)
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = AddEventFragmentBinding.inflate(layoutInflater, container, false)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }

        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
            }

        binding.addButton.setOnClickListener {
            Toast.makeText(
                context,
                temp?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                Toast.LENGTH_SHORT
            ).show()
            val appPerms = arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR,
            )
            activityResultLauncher.launch(appPerms)
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
        }

        binding.watch.setOnClickListener {
            eventId?.let { it1 -> goToCalendarIntent(it1) }
        }

        binding.receive.setOnClickListener {
            viewModel.getPlace(arguments?.get("place_id") as Int).observe(viewLifecycleOwner, { place ->
                val place1 = place as DBPlaceDTO
                eventId = place1.real_event_id
                Toast.makeText(
                    context,
                    eventId?.let { it1 -> CalendarHandler.getEvent(it1, requireContext()) + "Place_id: ${arguments?.get("place_id")}" },
                    Toast.LENGTH_SHORT
                ).show()
            })
        }

        return binding.root
    }

    private fun goToCalendarIntent(eventID: Long){
        val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID)
        val intent = Intent(Intent.ACTION_VIEW).setData(uri)
        startActivity(intent)
    }
}