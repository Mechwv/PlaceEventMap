package com.example.placeeventmap.presentation.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.placeeventmap.presentation.room.dto.DBPlaceDTO
import com.example.placeeventmap.presentation.room.BaseDao

@Dao
abstract class  PlaceDao : BaseDao<DBPlaceDTO> {

    @Query("SELECT * FROM places")
    abstract fun getAll(): List<DBPlaceDTO>
}