package com.mechwv.placeeventmap.presentation.events

import android.util.Log
import androidx.lifecycle.*
import com.mechwv.placeeventmap.domain.model.Event
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.room.EventRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val placeRep: PlaceRepositoryImpl,
    private val eventRep: EventRepositoryImpl,
): ViewModel() {

    fun getEvent(id: Long): LiveData<Event> {
        val event = eventRep.getEvent(id)
        Log.e("Place : ", event.value.toString())
        return event
    }


}