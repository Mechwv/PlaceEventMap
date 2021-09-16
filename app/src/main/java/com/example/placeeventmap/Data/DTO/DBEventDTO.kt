package com.example.placeeventmap.Data.DTO

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "events")
data class DBEventDTO(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "event_name") var name: String,
    @ColumnInfo(name = "event_description") var description: String? = null,
    @ColumnInfo(name = "event_time") var startTime: Timestamp,
    @ColumnInfo(name = "location_id") var locationId : Int
)