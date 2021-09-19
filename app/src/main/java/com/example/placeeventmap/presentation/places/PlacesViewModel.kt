package com.example.placeeventmap.presentation.places

import android.util.Log
import androidx.lifecycle.*
import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.room.PlaceRepositoryImpl
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

    fun getDBPlaces(): LiveData<List<Place>> {
        val a = repository.getPlaces()
        Log.e("Repository: ", a.value.toString())
        return a
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