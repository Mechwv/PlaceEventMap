package com.example.placeeventmap.presentation.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.placeeventmap.databinding.AddEventFragmentBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EventAddFragment: Fragment() {
    private lateinit var viewModel: EventAddViewModel
    private lateinit var binding: AddEventFragmentBinding
    var temp: LocalDateTime? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val calendar = Calendar.getInstance()
        binding = AddEventFragmentBinding.inflate(layoutInflater, container, false)
        binding.addButton.setOnClickListener {
            Toast.makeText(context, temp?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), Toast.LENGTH_SHORT).show()
        }

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
        return binding.root
    }

}