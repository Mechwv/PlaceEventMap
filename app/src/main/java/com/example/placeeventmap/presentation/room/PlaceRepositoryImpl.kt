package com.example.placeeventmap.presentation.room

import android.os.Handler
import android.os.Looper
import android.util.Log
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

    override fun addPlace(placeDTO: DBPlaceDTO) {
        executorService.execute {
            placeDao.insert(placeDTO)
        }
    }

    override fun addPlaces(placeDTO: List<DBPlaceDTO>) {
        executorService.execute {
            placeDao.insert(placeDTO)
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