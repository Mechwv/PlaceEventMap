package com.example.placeeventmap.presentation.retrofit

import android.util.Log
import com.example.placeeventmap.BuildConfig
import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.retrofit.model.GeoResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object Common {
    private val BASE_URL = "https://geocode-maps.yandex.ru/"
    val retrofitService: YandexApi
        get() = RetrofitClient.getClient(BASE_URL).create(YandexApi::class.java)


    @ExperimentalCoroutinesApi
    suspend fun getPlaceName(place: Place): String {
        return suspendCancellableCoroutine { continuation ->
            val mService = retrofitService
            var name: String = ""
//            Log.e("RETROFIT_REQUEST","${place.latitude},${place.longtitude}")
            mService.getPlaceName(BuildConfig.GEOCODER_KEY, "${place.longtitude},${place.latitude}").enqueue(object : Callback<GeoResult> {
                override fun onResponse(call: Call<GeoResult>, response: Response<GeoResult>) {
                    if (response.isSuccessful) {
                        val a = response.body()
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
                    val exception = IllegalStateException(response.errorBody()?.string())
                    Log.e("RETROFIT_ERROR", "Retrofit failure!")
                    continuation.resumeWithException(exception)
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

}