package com.mechwv.placeeventmap.presentation.retrofit.model

data class GeocoderMetaData(
    val Address: Address,
    val AddressDetails: AddressDetails,
    val kind: String,
    val precision: String,
    val text: String
)