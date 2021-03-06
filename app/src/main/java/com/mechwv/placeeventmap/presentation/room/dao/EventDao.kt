package com.mechwv.placeeventmap.presentation.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mechwv.placeeventmap.domain.model.Event
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import com.mechwv.placeeventmap.presentation.room.BaseDao
import com.mechwv.placeeventmap.presentation.room.dto.DBPlaceDTO

@Dao
abstract class EventDao : BaseDao<DBEventDTO> {

    @Insert
    abstract fun insertWithReturn(event: DBEventDTO): Long

    @Query("SELECT * FROM events")
    abstract fun getAll(): LiveData<List<DBEventDTO>>

    @Query("SELECT * FROM events where uid = :id")
    abstract fun getOne(id: Long): LiveData<DBEventDTO>

    @Query("update events set event_description = :desc, event_name = :name, event_time = :startTime where uid = :id")
    abstract fun updateEvent(id: Long, desc: String, name: String, startTime: String)

    @Query("update events set location_id = :place_id, place_name = 'Место еще не назначено' where uid = :id")
    abstract fun updatePlaceEvent(id: Long, place_id: Int?)

    @Query("update events set event_name = :name where uid = :id")
    abstract fun updateEventName(id: Long, name: String)
}