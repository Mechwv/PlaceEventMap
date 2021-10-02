package com.example.placeeventmap.domain.model

import java.time.LocalDateTime

open class Event(
    open var id: Int,
    open var name: String,
    open var description: String? = null,
    open var startTime: String,
    open var locationId : Int
)