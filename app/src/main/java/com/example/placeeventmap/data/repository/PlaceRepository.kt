package com.example.placeeventmap.data.repository

import androidx.lifecycle.LiveData
import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.room.dto.DBPlaceDTO

interface PlaceRepository {
    fun addPlace(place: Place)
    fun updatePlace(id: Int, event_id: Long)
    fun getPlace(id: Int): LiveData<Place>
    fun getPlaces(): LiveData<List<Place>>
    fun addPlaces(place: List<Place>)
    fun deletePlace(place: Place)
    fun deletePlaces(place: List<Place>)
}