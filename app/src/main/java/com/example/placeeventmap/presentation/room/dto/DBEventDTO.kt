package com.example.placeeventmap.presentation.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class DBEventDTO(
    @ColumnInfo(name = "event_name") var name: String,
    @ColumnInfo(name = "event_description") var description: String? = null,
    @ColumnInfo(name = "event_time") var startTime: String,
    @ColumnInfo(name = "location_id") var locationId : Int
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}