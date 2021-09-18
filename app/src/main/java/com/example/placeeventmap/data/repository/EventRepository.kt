package com.example.placeeventmap.data.repository

import com.example.placeeventmap.presentation.room.dto.DBEventDTO

interface EventRepository {
    fun addEvent(eventDTO: DBEventDTO)
}