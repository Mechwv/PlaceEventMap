package com.mechwv.placeeventmap.presentation.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PlaceRepositoryImpl
): ViewModel() {

//    private val markerDataList = mutableListOf<MarkerData>
//    private val markerDataList = MapObjectCollectionBinding

    fun getPlace(id: Int): LiveData<Place> {
        val place = repository.getPlace(id)
        Log.e("Place : ", place.value.toString())
        return place
    }



}