package com.example.placeeventmap.Data.Repository.Room

import com.example.placeeventmap.Data.DTO.DBEventDTO
import com.example.placeeventmap.Data.DTO.DBPlaceDTO
import com.example.placeeventmap.Data.Repository.Room.DAO.PlaceDao
import com.example.placeeventmap.Domain.Repository.PlaceRepository
import javax.inject.Inject

class PlaceRepositoryImpl(
    private val placeDao: PlaceDao
) : PlaceRepository {
    override suspend fun addPlace(placeDTO: DBPlaceDTO) {
        placeDao.insert(placeDTO)
    }

    override suspend fun getPlaces() {

    }
}