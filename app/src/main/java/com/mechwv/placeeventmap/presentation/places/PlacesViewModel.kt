package com.mechwv.placeeventmap.presentation.places

import android.util.Log
import androidx.lifecycle.*
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.room.EventRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val placeRep: PlaceRepositoryImpl,
    private val eventRep: EventRepositoryImpl
) : ViewModel() {
//    private val _places = MutableLiveData<List<Place>>()
//    val places: LiveData<List<Place>>
//        get() = _places

    fun getDBPlaces(filter: String = "cringe"): LiveData<List<Place>> {
        val allPlaces = placeRep.getPlaces()
        val fp = Transformations.switchMap(allPlaces) { placeList ->
            val filteredPlaces = MutableLiveData<List<Place>>()
            val filteredList = placeList.filter { p-> p.name.contains(filter, ignoreCase = true) }
            filteredPlaces.value = filteredList
            filteredPlaces
        }
        return fp
    }

    fun addPlace(place: Place) {
//        Log.e("New Place: ", place.toString())
        placeRep.addPlace(place)
    }

    fun deletePlace(place: Place) {
        Log.e("Place to delete: ", place.toString())
        placeRep.deletePlace(place)
        place.event_id?.let { eventRep.updatePlaceEvent(it, null) }
    }

    fun deletePlaces(place: List<Place>) {
        placeRep.deletePlaces(place)
    }

}