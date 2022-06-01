package com.mechwv.placeeventmap.presentation.dialogs

import android.util.Log
import androidx.lifecycle.*
import com.mechwv.placeeventmap.domain.model.Event
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.retrofit.model.geoApi.GeoPlace
import com.mechwv.placeeventmap.presentation.room.EventRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventCreateViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val placeRep: PlaceRepositoryImpl,
    private val eventRep: EventRepositoryImpl,
): ViewModel() {

    fun addEvent(event: Event): LiveData<Long>  {
        return liveData {
            val job = viewModelScope.launch {
                val e = eventRep.addEventWithReturn(event)
                emit(e)
            }
            job.join()
        }
    }

    fun addEventToPlace(placeId: Int, eventId: Long) {
        placeRep.updatePlaceEvent(placeId, eventId)
    }

    fun getPlace(id: Int): LiveData<Place> {
        val place = placeRep.getPlace(id)
        Log.e("Place : ", place.value.toString())
        return place
    }




}