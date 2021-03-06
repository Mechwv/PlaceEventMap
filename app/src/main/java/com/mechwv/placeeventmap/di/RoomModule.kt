package com.mechwv.placeeventmap.di

import android.content.Context
import androidx.room.Room
import com.mechwv.placeeventmap.domain.model.User
import com.mechwv.placeeventmap.presentation.room.AppDatabase
import com.mechwv.placeeventmap.presentation.room.dao.EventDao
import com.mechwv.placeeventmap.presentation.room.dao.PlaceDao
import com.mechwv.placeeventmap.presentation.room.dao.UserDao
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

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDAO()
    }
}