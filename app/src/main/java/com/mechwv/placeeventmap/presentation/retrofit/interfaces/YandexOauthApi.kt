package com.mechwv.placeeventmap.presentation.retrofit.interfaces

import com.mechwv.placeeventmap.BuildConfig
import com.mechwv.placeeventmap.presentation.retrofit.model.idApi.OauthToken
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import retrofit2.Call
import retrofit2.http.*

interface YandexOauthApi {
    @Headers("Content-Type: application/json")
    @POST("authorize")
    fun getToken(
        @Query("client_id") clientId: String,
        @Query("redirect_uri") redirect_uri: String=BuildConfig.REDIRECT_URI,
        @Query("response_type") type: String="token",
        @Query("login_hint") loginHint: String = ""
    ) : Call<OauthToken>


    @GET("info")
    fun getProfileInfo(
        @Header("Authorization") auth: String,
        @Query("format") format: String="json",
    ) : Call<ProfileInfo>

}