package com.example.placeeventmap.Data.DTO

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.placeeventmap.Domain.Model.Place
import java.sql.Timestamp

@Entity(tableName = "places")
data class DBPlaceDTO(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "location_name") var latitude: Double,
    @ColumnInfo(name = "location_name") var longtitude: Double,
    @ColumnInfo(name = "location_name") var name: String,
    @ColumnInfo(name = "location_description") var description: String? = null,
) {
    constructor(place: Place) : this(
        place.id,
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
