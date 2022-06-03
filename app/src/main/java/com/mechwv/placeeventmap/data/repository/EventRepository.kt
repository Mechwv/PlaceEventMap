package com.mechwv.placeeventmap.data.repository

import androidx.lifecycle.LiveData
import com.mechwv.placeeventmap.domain.model.Event
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO

interface EventRepository {
    fun addEvent(event: Event)
    fun addEventWithReturn(event: Event): Long
    fun getEvent(id: Long): LiveData<Event>
    fun getEvents(): LiveData<List<Event>>
    fun deleteEvent(event: Event)
    fun updatePlaceEvent(id: Long, place_id: Int?)
    fun updateEvent(event: Event)
}