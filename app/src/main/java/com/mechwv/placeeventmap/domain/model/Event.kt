package com.mechwv.placeeventmap.domain.model

open class Event(
    open var id: Int,
    open var name: String,
    open var description: String? = null,
    open var startTime: String,
    open var locationId : Int
)