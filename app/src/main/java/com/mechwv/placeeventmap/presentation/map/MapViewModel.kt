package com.mechwv.placeeventmap.presentation.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.retrofit.model.geoApi.GeoPlace
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
    fun getAddressByString(address: String): LiveData<GeoPlace> {
        return liveData {
            val addr = repository.getAddressByString(address)
            emit(addr)
        }
    }



}