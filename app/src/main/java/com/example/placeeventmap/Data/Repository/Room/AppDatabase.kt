package com.example.placeeventmap.Data.Repository.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.placeeventmap.Data.DTO.DBEventDTO
import com.example.placeeventmap.Data.DTO.DBPlaceDTO
import com.example.placeeventmap.Data.Repository.Room.DAO.EventDao
import com.example.placeeventmap.Data.Repository.Room.DAO.PlaceDao
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

}