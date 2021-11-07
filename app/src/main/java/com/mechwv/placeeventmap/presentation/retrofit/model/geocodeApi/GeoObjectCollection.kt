package com.mechwv.placeeventmap.presentation.retrofit.model.geocodeApi

data class GeoObjectCollection(
    val featureMember: List<FeatureMember>,
    val metaDataProperty: MetaDataPropertyX
)