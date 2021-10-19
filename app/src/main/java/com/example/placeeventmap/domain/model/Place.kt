package com.example.placeeventmap.domain.model

open class Place(
    open var id: Int = 0,
    open var latitude: Double,
    open var longtitude: Double,
    open var name: String,
    open var description: String?,
    open var event_id: Long?,
    open var address: String? = ""
)