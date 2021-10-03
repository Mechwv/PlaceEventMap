package com.example.placeeventmap.presentation.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.placeeventmap.presentation.room.dto.DBPlaceDTO
import com.example.placeeventmap.presentation.room.BaseDao

@Dao
abstract class  PlaceDao : BaseDao<DBPlaceDTO> {

    @Query("SELECT * FROM places where uid = :id")
    abstract fun getOne(id: Int): LiveData<DBPlaceDTO>

    @Query("SELECT * FROM places")
    abstract fun getAll(): LiveData<List<DBPlaceDTO>>
}