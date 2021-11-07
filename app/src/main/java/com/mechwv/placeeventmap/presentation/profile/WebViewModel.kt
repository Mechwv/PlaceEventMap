package com.mechwv.placeeventmap.presentation.profile

import android.util.Log
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import com.mechwv.placeeventmap.presentation.retrofit.Common
import com.mechwv.placeeventmap.presentation.room.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepositoryImpl
): ViewModel() {
    private val authService = Common

    fun getProfile(url: String, activity: FragmentActivity, login: String = ""): LiveData<ProfileInfo?> {

        val accessCodeFragment = "access_token="
        if (url.contains(accessCodeFragment)) {
            val accessCode = url.substring(url.indexOf(accessCodeFragment) + accessCodeFragment.length).substringBefore("&")
            Log.e("Access Token", accessCode)
            return liveData {
                try {
                    val profile = authService.getProfile(accessCode, activity)
                    repository.setProfile(profile)
                    emit(profile)
                } catch (e: Exception) {
                    Log.e("SET PROFILE ERROR", e.message.toString())
                }
            }

        }
        return liveData {
            emit(null)
        }
    }
}