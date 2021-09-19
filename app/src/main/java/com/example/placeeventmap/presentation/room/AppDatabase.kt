package com.example.placeeventmap.presentation.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.placeeventmap.presentation.room.dto.DBEventDTO
import com.example.placeeventmap.presentation.room.dto.DBPlaceDTO
import com.example.placeeventmap.presentation.room.dao.EventDao
import com.example.placeeventmap.presentation.room.dao.PlaceDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [DBPlaceDTO::class, DBEventDTO::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDAO(): EventDao
    abstract fun placeDAO(): PlaceDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }

}