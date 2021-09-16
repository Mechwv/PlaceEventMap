package com.example.placeeventmap.Data.Repository.Room

import androidx.room.Insert

interface BaseDao<T> {
    @Insert
    suspend fun insert(entity: T)

//    @Insert
//    suspend fun insert(entities: List<T>)

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