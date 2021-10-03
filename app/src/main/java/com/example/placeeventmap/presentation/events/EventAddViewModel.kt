package com.example.placeeventmap.presentation.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventAddViewModel @Inject constructor(
private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    val selectedDate: MutableLiveData<String> = MutableLiveData()
}