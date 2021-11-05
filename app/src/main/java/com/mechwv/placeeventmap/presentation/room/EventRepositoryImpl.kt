package com.mechwv.placeeventmap.presentation.room

import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import com.mechwv.placeeventmap.presentation.room.dao.EventDao
import com.mechwv.placeeventmap.data.repository.EventRepository

class EventRepositoryImpl
constructor(
    private val eventDao: EventDao
)  : EventRepository {
    override fun addEvent(eventDTO: DBEventDTO) {
        TODO("Not yet implemented")
    }

}