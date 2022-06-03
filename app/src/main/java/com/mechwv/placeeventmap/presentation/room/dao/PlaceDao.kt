package com.mechwv.placeeventmap.presentation.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mechwv.placeeventmap.presentation.room.dto.DBPlaceDTO
import com.mechwv.placeeventmap.presentation.room.BaseDao

@Dao
abstract class  PlaceDao : BaseDao<DBPlaceDTO> {

    @Query("SELECT * FROM places where uid = :id")
    abstract fun getOne(id: Int): LiveData<DBPlaceDTO>

    @Query("update places set event_id = :event_id where uid = :id")
    abstract fun updatePlaceEvent(id: Int, event_id: Long?)

    @Query("update places set location_name = :name where uid = :id")
    abstract fun updatePlaceName(id: Int, name: String)

    @Query("SELECT * FROM places")
    abstract fun getAll(): LiveData<List<DBPlaceDTO>>
}