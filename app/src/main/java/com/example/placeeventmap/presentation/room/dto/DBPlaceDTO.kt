package com.example.placeeventmap.presentation.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.placeeventmap.domain.model.Place

@Entity(tableName = "places")
data class DBPlaceDTO(
    @ColumnInfo(name = "location_latitude") var latitude: Double,
    @ColumnInfo(name = "location_longtitude") var longtitude: Double,
    @ColumnInfo(name = "location_name") var name: String,
    @ColumnInfo(name = "location_description") var description: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    constructor(place: Place) : this(
        place.latitude,
        place.longitude,
        place.name,
        place.description
    )

    fun toPlace() = Place(
        uid,
        latitude,
        longtitude,
        name,
        description
    )
}
