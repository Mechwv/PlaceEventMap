package com.example.placeeventmap.presentation.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.placeeventmap.domain.model.Place

@Entity(tableName = "places")
data class DBPlaceDTO(
    @PrimaryKey(autoGenerate = true)
    var uid: Int,
    @ColumnInfo(name = "location_latitude") override var latitude: Double,
    @ColumnInfo(name = "location_longtitude") override var longtitude: Double,
    @ColumnInfo(name = "location_name") override var name: String,
    @ColumnInfo(name = "location_description") override var description: String? = null,
    @ColumnInfo(name = "real_event_id") var real_event_id: Long? = null,
) : Place (
    uid,
    latitude,
    longtitude,
    name,
    description) {


    constructor(place: Place) : this(
        place.id,
        place.latitude,
        place.longtitude,
        place.name,
        place.description
    )

    fun toPlace() : Place {
        val place = Place(
            uid,
            latitude,
            longtitude,
            name,
            description
        )
        return place
    }
}
