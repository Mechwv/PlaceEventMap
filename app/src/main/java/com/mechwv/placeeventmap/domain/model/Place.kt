package com.mechwv.placeeventmap.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

open class Place(
    @JsonProperty("id")
    open var id: Int = 0,
    @JsonProperty("latitude")
    open var latitude: Double,
    @JsonProperty("longtitude")
    open var longtitude: Double,
    @JsonProperty("name")
    open var name: String,
    @JsonProperty("description")
    open var description: String?,
    @JsonProperty("event_id")
    open var event_id: Long?,
    @JsonProperty("address")
    open var address: String? = ""
)