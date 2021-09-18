package com.example.placeeventmap.data.repository

import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.room.dto.DBPlaceDTO

interface PlaceRepository {
    fun addPlace(placeDTO: DBPlaceDTO)
    fun getPlaces(callback: (List<Place>) -> Unit)
    fun addPlaces(placeDTO: List<DBPlaceDTO>)
}