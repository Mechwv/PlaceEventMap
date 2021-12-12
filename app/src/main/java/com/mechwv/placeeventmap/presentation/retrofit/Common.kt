package com.mechwv.placeeventmap.presentation.retrofit

import android.util.Log
import com.mechwv.placeeventmap.BuildConfig
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.retrofit.interfaces.YandexApi
import com.mechwv.placeeventmap.presentation.retrofit.interfaces.YandexOauthApi
import com.mechwv.placeeventmap.presentation.retrofit.model.geocodeApi.GeoResult
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import com.mechwv.placeeventmap.presentation.retrofit.interfaces.ServerApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object Common {
    private const val MAPS_BASE_URL = "https://geocode-maps.yandex.ru/"
    private val retrofitService: YandexApi
        get() = RetrofitClient.getClient(MAPS_BASE_URL).create(YandexApi::class.java)

    private const val ID_BASE_URL = "https://login.yandex.ru/"
    private val authService: YandexOauthApi
        get() = RetrofitClient.getClient(ID_BASE_URL).create(YandexOauthApi::class.java)

    private const val SERVER_BASE_URL = "http://136.243.64.85:9000/"
//    private const val SERVER_BASE_URL = "http://10.0.2.2:8080/"
    private val serverService: ServerApi
        get() = RetrofitClient.getClient(SERVER_BASE_URL).create(ServerApi::class.java)


    @ExperimentalCoroutinesApi
    suspend fun getPlaceName(place: Place): String {
        return suspendCancellableCoroutine { continuation ->
            val mService = retrofitService
            var name: String = ""
//            Log.e("RETROFIT_REQUEST","${place.latitude},${place.longtitude}")
            mService.getPlaceName(BuildConfig.GEOCODER_KEY, "${place.longtitude},${place.latitude}").enqueue(object : Callback<GeoResult> {
                override fun onResponse(call: Call<GeoResult>, response: Response<GeoResult>) {
                    if (response.isSuccessful) {
                        val searchResponse = (response.body() as GeoResult)
                            .response
                            .GeoObjectCollection
                            .featureMember
                        if (searchResponse.isNotEmpty()) {
                            name =
                                searchResponse[0].GeoObject.metaDataProperty.GeocoderMetaData.text
                            continuation.resume(name)
                            Log.e("RETROFIT_SUCCESS", name)
                            return
                        }
                    }
                    if (continuation.isActive) {
                        val exception = IllegalStateException(response.errorBody()?.string())
                        Log.e("RETROFIT_ERROR", "Retrofit failure!")
                        continuation.resumeWithException(exception)
                    }
                }

                override fun onFailure(call: Call<GeoResult>, t: Throwable) {
                    if (call.isCanceled) {
                        continuation.cancel()
                        return
                    }
                    Log.e("RETROFIT_ERROR", "Retrofit failure!", t)
                    continuation.resumeWithException(t)
                }
            })
        }
    }

//    }

    suspend fun getProfile(jwt_token: String): ProfileInfo {
        return suspendCancellableCoroutine { continuation ->
            serverService.getProfileInfo(auth= "Bearer $jwt_token").enqueue(object  : Callback<ProfileInfo> {
                override fun onResponse(call: Call<ProfileInfo>, response: Response<ProfileInfo>) {
                    if (response.isSuccessful) {
                        val authResponse = (response.body() as ProfileInfo)
                        continuation.resume(authResponse)
                        Log.e("PROFILE INFO", authResponse.toString())

                    }
                    if (continuation.isActive) {
                        val exception = IllegalStateException(response.errorBody()?.string())
                        continuation.resumeWithException(exception)
                    }
                }
                override fun onFailure(call: Call<ProfileInfo>, t: Throwable) {
                    Log.e("TOKEN", t.message!!)
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    suspend fun getPlaces(jwt_token: String): List<Place> {
        return suspendCancellableCoroutine { continuation ->
            serverService.getAllPlaces(auth= "Bearer $jwt_token").enqueue(object  : Callback<List<Place>> {
                override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                    if (response.isSuccessful) {
                        val placeResponse = (response.body() as List<Place>)
                        continuation.resume(placeResponse)
                        Log.e("PLACE INFO", placeResponse.toString())

                    }
                    if (continuation.isActive) {
                        val exception = IllegalStateException(response.errorBody()?.string())
                        continuation.resumeWithException(exception)
                    }
                }
                override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                    Log.e("PLACE", t.message!!)
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    suspend fun authorizeOnServer(auth_token: String, role: String): Response<ServerApi.JWTstring> {
        return serverService.authorize(ServerApi.AuthDTO(auth_token, role))
    }

}


//    suspend fun getProfile(access_code: String): ProfileInfo {
//        return suspendCancellableCoroutine { continuation ->
//            val mService = authService
//            mService.getProfileInfo(auth= "OAuth $access_code").enqueue(object  : Callback<ProfileInfo> {
//                override fun onResponse(call: Call<ProfileInfo>, response: Response<ProfileInfo>) {
//                    if (response.isSuccessful) {
//                        val authResponse = (response.body() as ProfileInfo)
//                        continuation.resume(authResponse)
//                        Log.e("PROFILE INFO", authResponse.toString())
//
//                    }
//                    if (continuation.isActive) {
//                        val exception = IllegalStateException(response.errorBody()?.string())
//                        continuation.resumeWithException(exception)
//                    }
//                }
//                override fun onFailure(call: Call<ProfileInfo>, t: Throwable) {
//                    Log.e("TOKEN", t.message!!)
//                    continuation.resumeWithException(t)
//                }
//            })
//        }