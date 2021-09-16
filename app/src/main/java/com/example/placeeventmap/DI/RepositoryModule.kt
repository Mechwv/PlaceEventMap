package com.example.placeeventmap.DI

import com.example.placeeventmap.Data.Repository.Room.DAO.EventDao
import com.example.placeeventmap.Data.Repository.Room.DAO.PlaceDao
import com.example.placeeventmap.Data.Repository.Room.EventRepositoryImpl
import com.example.placeeventmap.Data.Repository.Room.PlaceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideEventRepository(
        eventDao: EventDao
    ): EventRepositoryImpl {
        return EventRepositoryImpl(eventDao)
    }

    @Singleton
    @Provides
    fun providePlaceRepository(
        placeDao: PlaceDao
    ): PlaceRepositoryImpl {
        return PlaceRepositoryImpl(placeDao)
    }

}