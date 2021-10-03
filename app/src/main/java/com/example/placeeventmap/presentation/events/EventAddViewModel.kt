package com.example.placeeventmap.presentation.events

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EventAddViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PlaceRepositoryImpl
): ViewModel() {
//    val selectedDate: MutableLiveData<String> = MutableLiveData()

    fun updatePlace(id: Int, event_id: Long) {
        repository.updatePlace(id, event_id)
    }
    fun getPlace(id: Int): LiveData<Place> {
        return repository.getPlace(id)
    }
}