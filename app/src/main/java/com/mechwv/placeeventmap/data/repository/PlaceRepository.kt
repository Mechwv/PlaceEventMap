package com.mechwv.placeeventmap.data.repository

import androidx.lifecycle.LiveData
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.retrofit.model.geoApi.GeoPlace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Address

interface PlaceRepository {
    fun addPlace(place: Place)
    suspend fun getAddress(place: Place): String
    fun updatePlaceEvent(id: Int, event_id: Long)
    fun updatePlaceName(id: Int, name: String)
    fun getPlace(id: Int): LiveData<Place>
    fun getPlaces(): LiveData<List<Place>>
    fun addPlaces(place: List<Place>)
    fun deletePlace(place: Place)
    fun deletePlaces(place: List<Place>)
    @ExperimentalCoroutinesApi
    suspend fun getAddressByString(address: String): GeoPlace
}