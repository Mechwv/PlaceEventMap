package com.example.placeeventmap.di

import android.content.Context
import androidx.room.Room
import com.example.placeeventmap.presentation.room.AppDatabase
import com.example.placeeventmap.presentation.room.dao.EventDao
import com.example.placeeventmap.presentation.room.dao.PlaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDB(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
//            .createFromAsset("database/placeEventMap.db")
            .build()
    }

    @Singleton
    @Provides
    fun providePlaceDao(appDatabase: AppDatabase): PlaceDao {
        return appDatabase.placeDAO()
    }

    @Singleton
    @Provides
    fun provideEventDao(appDatabase: AppDatabase): EventDao {
        return appDatabase.eventDAO()
    }
}