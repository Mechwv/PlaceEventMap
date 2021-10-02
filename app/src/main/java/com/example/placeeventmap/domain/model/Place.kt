package com.example.placeeventmap.domain.model

open class Place(
    open var id: Int,
    open var latitude: Double,
    open var longtitude: Double,
    open var name: String,
    open var description: String?,
)