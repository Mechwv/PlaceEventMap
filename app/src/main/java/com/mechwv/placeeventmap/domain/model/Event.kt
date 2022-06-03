package com.mechwv.placeeventmap.domain.model

open class Event(
    open var id: Long = 0,
    open var name: String,
    open var description: String? = null,
    open var startTime: String,
    open var locationId : Int? = -1,
    open var placeName: String = "",
    open var calendarEventId: Long = 0
)