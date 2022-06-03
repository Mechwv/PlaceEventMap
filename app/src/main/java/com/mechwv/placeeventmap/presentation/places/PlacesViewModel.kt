package com.mechwv.placeeventmap.presentation.places

import android.util.Log
import androidx.lifecycle.*
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PlaceRepositoryImpl
) : ViewModel() {
//    private val _places = MutableLiveData<List<Place>>()
//    val places: LiveData<List<Place>>
//        get() = _places

    fun getDBPlaces(filter: String = "cringe"): LiveData<List<Place>> {
        val allPlaces = repository.getPlaces()
        val fp = Transformations.switchMap(allPlaces) { placeList ->
            val filteredPlaces = MutableLiveData<List<Place>>()
            val filteredList = placeList.filter { p-> p.name.contains(filter, ignoreCase = true) }
            filteredPlaces.value = filteredList
            filteredPlaces
        }
        return fp
//        return a
    }

    fun addPlace(place: Place) {
//        Log.e("New Place: ", place.toString())
        repository.addPlace(place)
    }

    fun deletePlace(place: Place) {
        Log.e("Place to delete: ", place.toString())
        repository.deletePlace(place)
    }

    fun deletePlaces(place: List<Place>) {
        repository.deletePlaces(place)
    }

}