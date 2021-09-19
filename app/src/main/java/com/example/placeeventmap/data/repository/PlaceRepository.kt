package com.example.placeeventmap.data.repository

import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.room.dto.DBPlaceDTO

interface PlaceRepository {
    fun addPlace(place: Place)
    fun getPlaces(callback: (List<Place>) -> Unit)
    fun addPlaces(place: List<Place>)
    fun deletePlace(place: Place)
    fun deletePlaces(place: List<Place>)
}