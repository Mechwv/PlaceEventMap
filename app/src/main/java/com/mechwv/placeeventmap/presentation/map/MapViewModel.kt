package com.mechwv.placeeventmap.presentation.map

import android.util.Log
import androidx.lifecycle.*
import com.mechwv.placeeventmap.domain.model.Event
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.retrofit.model.geoApi.GeoPlace
import com.mechwv.placeeventmap.presentation.room.EventRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val placeRep: PlaceRepositoryImpl,
    private val eventRep: EventRepositoryImpl,
): ViewModel() {

    val DBPlaces: LiveData<List<Place>> = placeRep.getPlaces()

//    private val markerDataList = mutableListOf<MarkerData>
//    private val markerDataList = MapObjectCollectionBinding

    fun getPlace(id: Int): LiveData<Place> {
        val place = placeRep.getPlace(id)
        Log.e("Place : ", place.value.toString())
        return place
    }

    fun getEventName(place: Place): LiveData<String> {
        val event = place.event_id?.let { eventRep.getEvent(it) }
        if (event != null) {
            val fe = Transformations.switchMap(event) { e ->
                val filteredEvents = MutableLiveData<String>()
                filteredEvents.value = e.name
                filteredEvents
            }
            return fe
        }
        return liveData {
            emit("")
        }
    }



    @ExperimentalCoroutinesApi
    fun getAddressByString(address: String): LiveData<GeoPlace> {
        return liveData {
            val addr = placeRep.getAddressByString(address)
            emit(addr)
        }
    }



}