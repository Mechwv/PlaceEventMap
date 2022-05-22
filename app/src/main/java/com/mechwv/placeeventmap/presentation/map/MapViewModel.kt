package com.mechwv.placeeventmap.presentation.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PlaceRepositoryImpl
): ViewModel() {

    fun getPlace(id: Int): LiveData<Place> {
        val place = repository.getPlace(id)
        Log.e("Place : ", place.value.toString())
        return place
    }

    val listener = object : InputListener {
        override fun onMapLongTap(p0: Map, p1: Point) {}

        override fun onMapTap(p0: Map, p1: Point) {
            Log.d("Map touch","You have touched $p1")
        }
    }
}