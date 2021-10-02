package com.example.placeeventmap.presentation.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.placeeventmap.domain.model.Event
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entity(tableName = "events")
data class DBEventDTO(
    @PrimaryKey(autoGenerate = true)
    var uid: Int,
    @ColumnInfo(name = "event_name") override var  name: String,
    @ColumnInfo(name = "event_description") override var  description: String? = null,
    @ColumnInfo(name = "event_time") var eventStartTime: String,
    @ColumnInfo(name = "location_id") override var  locationId : Int
): Event(
    uid,
    name,
    description,
    eventStartTime,
    locationId
) {
//    fun convert() {
//        val secondFormatter = DateTimeFormatter.ofPattern("MM/dd/uuuu hh:mm:ss a", Locale.)
//        re
//    }
}