package com.example.placeeventmap.Data.Repository.Room.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.placeeventmap.Data.DTO.DBPlaceDTO
import com.example.placeeventmap.Data.Repository.Room.BaseDao

@Dao
abstract class  PlaceDao : BaseDao<DBPlaceDTO> {

    @Query("SELECT * FROM places")
    abstract suspend fun getAll(): List<DBPlaceDTO>
}