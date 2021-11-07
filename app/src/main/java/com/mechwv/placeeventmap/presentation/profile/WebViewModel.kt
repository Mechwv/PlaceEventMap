package com.mechwv.placeeventmap.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import com.mechwv.placeeventmap.domain.model.Role
import com.mechwv.placeeventmap.presentation.retrofit.Common
import com.mechwv.placeeventmap.presentation.room.UserRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.dto.DBUserDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepositoryImpl
): ViewModel() {
    private val authService = Common

    fun getProfile(url: String, login: String = ""): LiveData<ProfileInfo?> {

        val accessCodeFragment = "access_token="
        if (url.contains(accessCodeFragment)) {
            val accessCode = url.substring(url.indexOf(accessCodeFragment) + accessCodeFragment.length).substringBefore("&")
            Log.e("Access Token", accessCode)
            return liveData {
                try {
                    val profile = authService.getProfile(accessCode)
                    repository.setProfile(profile)
                    val user = DBUserDTO(0, profile.default_email!!, "", Role.ADMIN.toString(), accessCode)
                    repository.setOauthUser(user)
                    repository.setCurrentUser(user)
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