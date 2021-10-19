package com.example.placeeventmap.presentation.retrofit

import com.example.placeeventmap.presentation.retrofit.model.GeoResult
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
    ): Call<GeoResult>

}