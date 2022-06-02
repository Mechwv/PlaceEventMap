package com.mechwv.placeeventmap.domain.model

open class Event(
    open var id: Long = 0,
    open var name: String,
    open var description: String? = null,
    open var startTime: String,
    open var locationId : Int,
    open var placeName: String

)