package com.mechwv.placeeventmap.presentation.retrofit.model

data class Country(
    val AddressLine: String,
    val AdministrativeArea: AdministrativeArea,
    val CountryName: String,
    val CountryNameCode: String
)