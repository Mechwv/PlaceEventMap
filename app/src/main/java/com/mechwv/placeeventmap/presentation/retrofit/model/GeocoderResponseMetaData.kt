package com.mechwv.placeeventmap.presentation.retrofit.model

data class GeocoderResponseMetaData(
    val Point: PointX,
    val found: String,
    val request: String,
    val results: String
)