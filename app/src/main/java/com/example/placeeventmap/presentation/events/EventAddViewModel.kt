package com.example.placeeventmap.presentation.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventAddViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PlaceRepositoryImpl
): ViewModel() {
//    val selectedDate: MutableLiveData<String> = MutableLiveData()

    fun updatePlace(id: Int, event_id: Long) {
        repository.updatePlaceEvent(id, event_id)
    }
    fun getPlace(id: Int): LiveData<Place> {
        return repository.getPlace(id)
    }
}