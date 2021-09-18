package com.example.placeeventmap.presentation.room

import com.example.placeeventmap.presentation.room.dto.DBEventDTO
import com.example.placeeventmap.presentation.room.dao.EventDao
import com.example.placeeventmap.data.repository.EventRepository

class EventRepositoryImpl
constructor(
    private val eventDao: EventDao
)  : EventRepository {
    override fun addEvent(eventDTO: DBEventDTO) {
        TODO("Not yet implemented")
    }

}