package com.example.placeeventmap.domain.model

data class Place(
    var latitude: Double,
    var longitude: Double,
    var name: String,
    var description: String?,
) {
    var id: Int = 0
}