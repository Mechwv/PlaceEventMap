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
    private var places: LiveData<List<DBPlaceDTO>> = placeDao.getAll()


    override fun addPlace(place: Place) {
        executorService.execute {
            placeDao.insert(DBPlaceDTO(place))
        }
    }

    override fun getPlace(id: Int): LiveData<Place> {
        return placeDao.getOne(id) as LiveData<Place>
    }

    override fun addPlaces(place: List<Place>) {
        executorService.execute {
            placeDao.insert(place.map { DBPlaceDTO(it) })
        }
    }

    override fun deletePlace(place: Place) {
        executorService.execute {
            placeDao.delete(place as DBPlaceDTO)
        }
    }

    override fun deletePlaces(place: List<Place>) {
        executorService.execute {
            placeDao.delete(place.map { DBPlaceDTO(it) })
        }
    }

    override fun getPlaces(): LiveData<List<Place>> {
        return places as LiveData<List<Place>>
    }
}