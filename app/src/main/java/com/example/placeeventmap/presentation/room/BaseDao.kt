package com.example.placeeventmap.presentation.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {
    @Insert
    fun insert(entity: T)

    @Insert
    fun insert(entities: List<T>)

//    @Update
//    suspend fun update(entity: T)
//
//    @Update
//    suspend fun update(entities: List<T>)

    @Delete
    fun delete(entity: T)

    @Delete
    fun delete(entities: List<T>)
}