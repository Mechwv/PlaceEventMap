package com.example.placeeventmap.Domain.Repository

import com.example.placeeventmap.Data.DTO.DBEventDTO

interface EventRepository {
    suspend fun addEvent(eventDTO: DBEventDTO)
}