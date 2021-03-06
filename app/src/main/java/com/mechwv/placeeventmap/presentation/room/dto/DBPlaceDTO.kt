package com.mechwv.placeeventmap.presentation.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mechwv.placeeventmap.domain.model.Place

@Entity(tableName = "places")
data class DBPlaceDTO(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "location_latitude") override var latitude: Double,
    @ColumnInfo(name = "location_longitude") override var longitude: Double,
    @ColumnInfo(name = "location_name") override var name: String,
    @ColumnInfo(name = "location_description") override var description: String = "",
    @ColumnInfo(name = "event_id") override var event_id: Long? = null,
    @ColumnInfo(name = "address") override var address: String? = "",
) : Place (
    uid,
    latitude,
    longitude,
    name,
    description,
    event_id,
    address) {


    constructor(place: Place) : this(
        place.id,
        place.latitude,
        place.longitude,
        place.name,
        place.description,
        place.event_id,
        place.address
    )

    companion object {
        fun DBPlaceDTO.toPlace(): Place {
            return Place(
                id = this.id,
                latitude = this.latitude,
                longitude = this.longitude,
                name = this.name,
                description = this.description,
                event_id = this.event_id,
                address = this.address
            )
        }
    }
}
