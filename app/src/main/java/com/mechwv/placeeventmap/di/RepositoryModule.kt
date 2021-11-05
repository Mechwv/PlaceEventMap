package com.mechwv.placeeventmap.di

import com.mechwv.placeeventmap.presentation.room.dao.EventDao
import com.mechwv.placeeventmap.presentation.room.dao.PlaceDao
import com.mechwv.placeeventmap.presentation.room.EventRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
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