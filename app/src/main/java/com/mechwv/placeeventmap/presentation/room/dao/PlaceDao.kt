package com.mechwv.placeeventmap.presentation.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.room.dto.DBPlaceDTO
import com.mechwv.placeeventmap.presentation.room.BaseDao

@Dao
interface  PlaceDao : BaseDao<DBPlaceDTO> {

    @Query("SELECT * FROM places where uid = :id")
    fun getOne(id: Int): LiveData<DBPlaceDTO>

    @Query("update places set event_id = :event_id where uid = :id")
    fun updatePlaceEvent(id: Int, event_id: Long?)

    @Query("update places set location_name = :name where uid = :id")
     fun updatePlaceName(id: Int, name: String)

    @Query("SELECT * FROM places")
     fun getAll(): LiveData<List<DBPlaceDTO>>

    @Query("DELETE FROM places")
     fun deleteAll()

    @Transaction
    fun updateWithDownloadedValues(places: List<Place>) {
        deleteAll()
        insert(places.map { DBPlaceDTO(it) })
    }
}