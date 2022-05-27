package com.mechwv.placeeventmap.presentation.room

import android.util.Log
import androidx.lifecycle.LiveData
import com.mechwv.placeeventmap.presentation.room.dto.DBPlaceDTO
import com.mechwv.placeeventmap.presentation.room.dao.PlaceDao
import com.mechwv.placeeventmap.data.repository.PlaceRepository
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.retrofit.Common
import com.mechwv.placeeventmap.presentation.retrofit.model.geoApi.GeoPlace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Address
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PlaceRepositoryImpl
constructor(
    private val placeDao: PlaceDao
) : PlaceRepository
{
    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private var places: LiveData<List<DBPlaceDTO>> = placeDao.getAll()


    override fun addPlace(place: Place) {
        executorService.execute {
            placeDao.insert(DBPlaceDTO(place))
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getAddress(place: Place): String {
        try {
            return  Common.getPlaceName(place = place).name
        } catch (e: Exception) {
            Log.e("123123", "Pepega")
        }
        return ""
    }

    @ExperimentalCoroutinesApi
    override suspend fun getAddressByString(address: String): GeoPlace {
        try {
            return  Common.getPlaceName(address = address)
        } catch (e: Exception) {
            Log.e("123123", "Pepega")
        }
        return GeoPlace()
    }

    override fun updatePlaceEvent(id: Int, event_id: Long) {
        executorService.execute {
            placeDao.updatePlaceEvent(id, event_id)
        }
    }

    override fun updatePlaceName(id: Int, name: String) {
        executorService.execute {
            placeDao.updatePlaceName(id, name)
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