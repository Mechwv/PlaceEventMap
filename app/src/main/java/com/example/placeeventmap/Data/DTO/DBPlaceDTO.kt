package com.example.placeeventmap.Data.DTO

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "places")
data class DBPlaceDTO(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "location_name") var latitude: Double,
    @ColumnInfo(name = "location_name") var longtitude: Double,
    @ColumnInfo(name = "location_name") var name: String,
    @ColumnInfo(name = "location_description") var description: String? = null,
)