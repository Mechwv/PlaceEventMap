package com.mechwv.placeeventmap.presentation.retrofit.interfaces

import com.mechwv.placeeventmap.presentation.retrofit.model.geoApi.GeocodeResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface YandexApi {
    @Headers("Content-Type: application/json" )
    @GET("1.x")
    fun getPlaceName(
        @Query("apikey") key: String,
        @Query("geocode") geocode: String,
        @Query("format") format: String = "json",
        @Query("results") results: String = "1"
    ): Call<GeocodeResult>

}