package com.mechwv.placeeventmap.presentation.profile

import android.content.Context
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.android.material.internal.ContextUtils.getActivity
import kotlin.coroutines.coroutineContext


@HiltViewModel
class WebViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepositoryImpl
): ViewModel() {
    private val commonService = Common

    fun getJwtToken(token: String): LiveData<String?> {
        Log.e("YA_AUTH_TOKEN", token)
        return liveData {
            var jwtToken: String? = ""
            try {
                val job = CoroutineScope(coroutineContext).launch {
                    val res = commonService.authorizeOnServer(
                        token,
                        Role.MODERATOR.role
                    )
                    if (res.isSuccessful) {
                        jwtToken = res.body()!!.jwtString
                    }
                }
                job.join()
                emit(jwtToken)
                Log.e("JWT", jwtToken.toString())
            } catch (e: Exception) {
                emit(null)
                Log.e("JWT ERROR", e.message.toString())
            }
        }
    }

    fun getProfile(jwtToken: String, login: String = ""): LiveData<ProfileInfo?> {
        return liveData {
            try {
                val profile = commonService.getProfile(jwtToken)
                repository.setProfile(profile)
                val user = DBUserDTO(0, profile.default_email!!, "", Role.MODERATOR.toString(), jwtToken)
                repository.setOauthUser(user)
                repository.setCurrentUser(user)
                emit(profile)
            } catch (e: Exception) {
                Log.e("SET PROFILE ERROR", e.message.toString())
            }
        }
    }
}