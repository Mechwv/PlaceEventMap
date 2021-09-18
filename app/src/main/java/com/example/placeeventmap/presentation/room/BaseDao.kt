package com.example.placeeventmap.presentation.room

import androidx.room.Insert

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

//    @Delete
//    suspend fun delete(entity: T)
//
//    @Delete
//    suspend fun delete(entities: List<T>)
}