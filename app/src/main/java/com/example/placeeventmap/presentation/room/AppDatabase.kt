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

        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
    }

    @Volatile private var INSTANCE: AppDatabase? = null

    fun prepopulate() {
        val PREPOPULATE_DATA = listOf(
            DBPlaceDTO(0.0, 0.0, "aboba", "cool place"),
            DBPlaceDTO(123.0, 123.0, "dota2", "awful place"),
            DBPlaceDTO(223.0, 546.0, "pepe", "froggy place"))

        placeDAO().insert(PREPOPULATE_DATA)
    }


}