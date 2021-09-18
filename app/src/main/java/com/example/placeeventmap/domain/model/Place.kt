package com.example.placeeventmap.domain.model

data class Place(
    var id: Int,
    var latitude: Double,
    var longitude: Double,
    var name: String,
    var description: String?,
)