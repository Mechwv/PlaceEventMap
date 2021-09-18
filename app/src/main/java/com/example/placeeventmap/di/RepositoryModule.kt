package com.example.placeeventmap.di

import com.example.placeeventmap.presentation.room.dao.EventDao
import com.example.placeeventmap.presentation.room.dao.PlaceDao
import com.example.placeeventmap.presentation.room.EventRepositoryImpl
import com.example.placeeventmap.presentation.room.PlaceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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