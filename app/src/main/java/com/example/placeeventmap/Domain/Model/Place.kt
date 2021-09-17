package com.example.placeeventmap.Domain.Model

data class Place(
    var id: Int,
    var latitude: Double,
    var longitude: Double,
    var name: String,
    var description: String?,
)