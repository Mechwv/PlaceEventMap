package com.mechwv.placeeventmap.presentation.retrofit.model.geocodeApi

data class Address(
    val Components: List<Component>,
    val country_code: String,
    val formatted: String
)