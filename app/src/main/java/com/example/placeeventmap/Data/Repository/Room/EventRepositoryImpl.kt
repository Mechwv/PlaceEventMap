package com.example.placeeventmap.Data.Repository.Room

import com.example.placeeventmap.Data.DTO.DBEventDTO
import com.example.placeeventmap.Data.DTO.DBPlaceDTO
import com.example.placeeventmap.Data.Repository.Room.DAO.EventDao
import com.example.placeeventmap.Domain.Repository.EventRepository
import javax.inject.Inject

class EventRepositoryImpl(
    private val eventDao: EventDao
)  : EventRepository{
    override suspend fun addEvent(eventDTO: DBEventDTO) {
        TODO("Not yet implemented")
    }

}