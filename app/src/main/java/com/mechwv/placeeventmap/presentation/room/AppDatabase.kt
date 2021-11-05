package com.mechwv.placeeventmap.presentation.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import com.mechwv.placeeventmap.presentation.room.dto.DBPlaceDTO
import com.mechwv.placeeventmap.presentation.room.dao.EventDao
import com.mechwv.placeeventmap.presentation.room.dao.PlaceDao

@Database(entities = [DBPlaceDTO::class, DBEventDTO::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDAO(): EventDao
    abstract fun placeDAO(): PlaceDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }

}