package com.example.placeeventmap.domain.model

import java.sql.Timestamp

data class Event(
    var name: String,
    var description: String? = null,
    var startTime: Timestamp,
    var locationId : Int
)