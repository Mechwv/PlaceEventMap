package com.example.placeeventmap.domain.model

open class Place(
    open var id: Int = 0,
    open var latitude: Double,
    open var longtitude: Double,
    open var name: String,
    open var description: String?,
) {
    constructor(latitude: Double, longtitude: Double, name: String) : this(0, latitude, longtitude, name, null) {

    }
}