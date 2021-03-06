package com.mechwv.placeeventmap.presentation.dialogs

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.mechwv.placeeventmap.R
import com.mechwv.placeeventmap.databinding.EventCreateDialogBinding
import com.mechwv.placeeventmap.domain.model.Event
import com.mechwv.placeeventmap.presentation.events.CalendarHandler
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class EventCreateDialog : DialogFragment() {
    private lateinit var binding: EventCreateDialogBinding
    private val viewModel: EventCreateViewModel by viewModels()
    var placeName: String = ""
    var address: String = ""
    var placeUid = 0
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
                if (temp != null) {
                    eventId = CalendarHandler.useCalendar(
                        calendar,
                        binding.eventNameText.text.toString(),
                        "Адрес: ${binding.placeNameText.text}\nОписание: ${binding.descText.text}",
                        address,
                        requireContext()
                    )


                    viewModel.addEvent(
                        Event(
                            name = binding.eventNameText.text.toString(),
                            description = binding.descText.text.toString(),
                            eventStartTime = temp?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                                .toString(),
                            locationId = placeUid,
                            placeName = placeName,
                            calendarEventId = eventId!!
                        )
                    ).observe(viewLifecycleOwner) { eventId ->
                        viewModel.addEventToPlace(placeUid, eventId)
                    }
                } else {
                    Toast.makeText(context, "Выберите время", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1.
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
        if (arguments != null) {
            val mArgs = requireArguments()
            placeName = mArgs.getString("name").toString()
            address = mArgs.getString("address").toString()
            placeUid = mArgs.getInt("uid")
        }
    }

    private fun goToCalendarIntent(eventID: Long){
        val uri: Uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID)
        val intent = Intent(Intent.ACTION_VIEW).setData(uri)
        startActivity(intent)
    }

    /** The system calls this to get the DialogFragment's layout, regardless
    of whether it's being displayed as a dialog or an embedded fragment. */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EventCreateDialogBinding.inflate(layoutInflater, container, false)
        //TODO databinding
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

//            if (eventId != null) { }
//            dismiss()
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
            eventId?.let { it1 -> goToCalendarIntent(it1) }
        }

        binding.receive.setOnClickListener {
            Toast.makeText(context, eventId?.let { it1 ->
                CalendarHandler.getEvent(
                    it1,
                    requireContext()
                )
            } + "Place_id: ${arguments?.get("place_id")}", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        // 3.
//        Log.d("FRAGMENT", "$longitude + $latitude")
        binding.placeNameText.text = placeName
//        binding.longitudeText.setText(longitude.toString())

        requireDialog().window?.setWindowAnimations(
            R.style.DialogAnimation
        )
    }
}