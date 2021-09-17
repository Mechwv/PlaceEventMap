package com.example.placeeventmap.Domain.Repository

import com.example.placeeventmap.Data.DTO.DBEventDTO
import com.example.placeeventmap.Data.DTO.DBPlaceDTO

interface PlaceRepository {
    suspend fun addPlace(placeDTO: DBPlaceDTO)
    suspend fun getPlaces()
}