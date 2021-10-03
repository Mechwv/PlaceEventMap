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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.placeeventmap.databinding.AddEventFragmentBinding
import com.example.placeeventmap.presentation.places.PlacesListFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class EventAddFragment: Fragment() {
    private lateinit var viewModel: EventAddViewModel
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
                useCalendar()
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
            Toast.makeText(
                context,
                eventId?.let { it1 -> getEvent(it1) },
                Toast.LENGTH_SHORT
            ).show()
        }
        return binding.root
    }

    private fun useCalendar() {
        val datetime = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, calendar.timeInMillis)
            put(CalendarContract.Events.DTEND,calendar.timeInMillis + 60000*60)
            put(CalendarContract.Events.TITLE, "Dance club")
            put(CalendarContract.Events.DESCRIPTION, "Group workout")
            put(CalendarContract.Events.CALENDAR_ID, 3)
            put(CalendarContract.Events.EVENT_TIMEZONE, TIMEZONE_TYPE_AUTO)
        }
        runBlocking {
            launch {
                val contentResolver = context?.contentResolver
                val uri: Uri? = contentResolver?.insert(CalendarContract.Events.CONTENT_URI, datetime)
                eventId = uri?.lastPathSegment?.toLong()
            }
        }
    }

    private fun getEvent(eventID: Long): String {
//        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID)
        val contentResolver = context?.contentResolver

        val EVENT_PROJECTION: Array<String> = arrayOf(
            CalendarContract.Events.CALENDAR_ID,                     // 0
            CalendarContract.Events.TITLE,            // 1
            CalendarContract.Events.DESCRIPTION,   // 2
            CalendarContract.Calendars.OWNER_ACCOUNT            // 3
        )

        val PROJECTION_CALENDAR_ID: Int = 0
        val PROJECTION_TITLE: Int = 1
        val PROJECTION_DESCRIPTION: Int = 2
        val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

//        val selection: String = "_id = ?"
//        val selectionArgs: Array<String> = arrayOf(eventID.toString())
//        val cur: Cursor? = contentResolver?.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)
        val cur: Cursor? = contentResolver?.query(uri, EVENT_PROJECTION, "", arrayOf(), null)
        if (cur != null) {
            while (cur.moveToNext()) {
                val calID: Long = cur.getLong(PROJECTION_CALENDAR_ID)
                val displayName: String = cur.getString(PROJECTION_TITLE)
                val desc: String = cur.getString(PROJECTION_DESCRIPTION)
                return "$calID  +  $displayName  +  $desc + Place_id: ${arguments?.get("place_id")}"
            }
        }
       return ""
    }

    private fun goToCalendarIntent(eventID: Long){
        val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID)
        val intent = Intent(Intent.ACTION_VIEW).setData(uri)
        startActivity(intent)
    }
}