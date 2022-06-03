package com.mechwv.placeeventmap.presentation.places

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mechwv.placeeventmap.domain.model.Event
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.room.EventRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@HiltViewModel
class PlacesInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val placeRep: PlaceRepositoryImpl,
    private val eventRep: EventRepositoryImpl,
) : ViewModel() {
    lateinit var place: LiveData<Place>

    fun getPlace(id: Int): LiveData<Place> {
        val pl = placeRep.getPlace(id)
            Log.e("Place : ", pl.value.toString())
        place = pl
        return place
    }

    fun getEvent(id: Long): LiveData<Event> {
        return eventRep.getEvent(id)
    }

    @ExperimentalCoroutinesApi
    fun getAddress(place: Place): LiveData<String> {
        return liveData {
            val addr = placeRep.getAddress(place)
            emit(addr)
        }
    }

    fun addPlace(place: Place) {
        placeRep.addPlace(place)
    }


    fun updatePlace(place: Place) {
        placeRep.updatePlace(place)
    }


}