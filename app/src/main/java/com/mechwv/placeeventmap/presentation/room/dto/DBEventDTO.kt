package com.mechwv.placeeventmap.presentation.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mechwv.placeeventmap.domain.model.Event

@Entity(tableName = "events")
data class DBEventDTO(
    @PrimaryKey(autoGenerate = true)
    var uid: Long,
    @ColumnInfo(name = "event_name") override var  name: String,
    @ColumnInfo(name = "event_description") override var  description: String? = null,
    @ColumnInfo(name = "event_time") override var eventStartTime: String,
    @ColumnInfo(name = "location_id") override var  locationId : Int?,
    @ColumnInfo(name = "place_name") override var placeName: String,
    @ColumnInfo(name = "calendar_event_id") override var calendarEventId: Long,
): Event(
    uid,
    name,
    description,
    eventStartTime,
    locationId,
    placeName,
    calendarEventId
) {
   constructor(event: Event) : this(
       event.id,
       event.name,
       event.description,
       event.eventStartTime,
       event.locationId,
       event.placeName,
       event.calendarEventId
    )
}