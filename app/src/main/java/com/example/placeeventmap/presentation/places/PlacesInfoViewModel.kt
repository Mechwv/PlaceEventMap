package com.example.placeeventmap.presentation.places

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.placeeventmap.BuildConfig
import com.example.placeeventmap.domain.model.Place
import com.example.placeeventmap.presentation.room.PlaceRepositoryImpl
import com.example.placeeventmap.util.retrofit.Common
import com.example.placeeventmap.util.retrofit.model.GeoResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class PlacesInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: PlaceRepositoryImpl
) : ViewModel() {

    fun getPlace(id: Int): LiveData<Place> {
        val place = repository.getPlace(id)
            Log.e("Place : ", place.value.toString())
        return place
    }

    fun addPlace(place: Place) {
        repository.addPlace(place)
    }

    @ExperimentalCoroutinesApi
    suspend fun getPlaceName(place: Place): String {
        return suspendCancellableCoroutine { continuation ->
            val mService = Common.retrofitService
            var name: String = ""
            mService.getPlaceName(BuildConfig.GEOCODER_KEY, "${place.latitude},${place.longtitude}").enqueue(object : Callback<GeoResult> {
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
                        Log.e("RETROFIT_ERROR", "Retrofit failure!", exception)
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