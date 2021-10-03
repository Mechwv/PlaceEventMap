package com.example.placeeventmap.presentation.places

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlacesInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PlaceRepositoryImpl
) : ViewModel() {

    fun getPlace(id: Int): LiveData<Place> {
        val place = repository.getPlace(id)
            Log.e("Place : ", place.value.toString())
        return place
    }

    fun addPlace(place: Place) {
        repository.addPlace(place)
    }

}