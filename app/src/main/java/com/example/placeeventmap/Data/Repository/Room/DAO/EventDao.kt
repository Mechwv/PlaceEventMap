package com.example.placeeventmap.Data.Repository.Room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.placeeventmap.Data.DTO.DBEventDTO
import com.example.placeeventmap.Data.Repository.Room.BaseDao

@Dao
abstract class EventDao : BaseDao<DBEventDTO> {

    @Query("SELECT * FROM events")
    abstract suspend fun getAll(): List<DBEventDTO>
}