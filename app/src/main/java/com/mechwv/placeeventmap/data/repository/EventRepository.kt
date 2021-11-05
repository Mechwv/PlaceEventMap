package com.mechwv.placeeventmap.data.repository

import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO

interface EventRepository {
    fun addEvent(eventDTO: DBEventDTO)
}