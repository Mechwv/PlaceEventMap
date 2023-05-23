package com.mechwv.placeeventmap.presentation.retrofit.interfaces

import com.fasterxml.jackson.annotation.JsonProperty
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ServerApi {
    @POST("auth")
    suspend fun authorize(
       @Body authDTO: AuthDTO
    ) : Response<JWTstring>

    @GET("/profile/info")
    fun getProfileInfo(
        @Header("Authorization") auth: String,
    ) : Call<ProfileInfo>

    @JvmSuppressWildcards
    @POST("/places/save")
    fun saveCurrentPlaces(
        @Header("Authorization") auth: String,
        @Body places: List<Place>
    ): Call<Boolean>

    @GET("/places/download")
    fun getOnlinePlaces(
        @Header("Authorization") auth: String,
    ) : Call<List<Place>>

    @GET("/places/all")
    fun getAllPlaces(
        @Header("Authorization") auth: String,
    ) : Call<List<Place>>

    data class AuthDTO(
        val authToken: String,
        val role: String
    )

    data class JWTstring(
        @JsonProperty("jwtString")
        val jwtString: String
    )
}