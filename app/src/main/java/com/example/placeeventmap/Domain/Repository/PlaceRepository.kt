package com.example.placeeventmap.Domain.Repository

import com.example.placeeventmap.Data.DTO.DBEventDTO

interface PlaceRepository {
    suspend fun addPlace(placeDTO: DBEventDTO)
}