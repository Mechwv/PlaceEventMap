package com.example.placeeventmap.presentation.room

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.placeeventmap.presentation.room.dto.DBPlaceDTO
import com.example.placeeventmap.presentation.room.dao.PlaceDao
import com.example.placeeventmap.data.repository.PlaceRepository
import com.example.placeeventmap.domain.model.Place
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PlaceRepositoryImpl
constructor(
    private val placeDao: PlaceDao
) : PlaceRepository {
    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }
//    private var places: LiveData<List<DBPlaceDTO>> = placeDao.getAll() as LiveData<List<DBPlaceDTO>>


    override fun addPlace(place: Place) {
        executorService.execute {
            placeDao.insert(DBPlaceDTO(place))
        }
    }

    override fun addPlaces(place: List<Place>) {
        executorService.execute {
            placeDao.insert(place.map { DBPlaceDTO(it) })
        }
    }

    override fun deletePlace(place: Place) {
        executorService.execute {
            placeDao.delete(DBPlaceDTO(place))
        }
    }

    override fun deletePlaces(place: List<Place>) {
        executorService.execute {
            placeDao.delete(place.map { DBPlaceDTO(it) })
        }
    }

    override fun getPlaces(callback: (List<Place>) -> Unit ) {
        executorService.execute {
            val placesDto = placeDao.getAll()
            val places = placesDto.map { it.toPlace() }
            Log.e("PlaceRep: ", places.toString())
            mainThreadHandler.post { callback(places) }
        }
    }
}