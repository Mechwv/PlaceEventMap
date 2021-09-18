package com.example.placeeventmap.presentation.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.placeeventmap.presentation.room.dto.DBEventDTO
import com.example.placeeventmap.presentation.room.BaseDao

@Dao
abstract class EventDao : BaseDao<DBEventDTO> {

    @Query("SELECT * FROM events")
    abstract fun getAll(): List<DBEventDTO>
}