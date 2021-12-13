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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class WebViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepositoryImpl
): ViewModel() {
    private val commonService = Common

    fun getProfile(url: String, login: String = ""): LiveData<ProfileInfo?> {

        val accessCodeFragment = "access_token="

        val handler = CoroutineExceptionHandler { _, exception ->
            println("123 got $exception")
        }

        if (url.contains(accessCodeFragment)) {
            Log.e("Result string", url)
            val accessCode = url.substring(url.indexOf(accessCodeFragment) + accessCodeFragment.length).substringBefore("&")
            Log.e("Access Token", accessCode)
            try {
                var jwtToken: String? = ""
                try {
                    runBlocking {
                        val job = GlobalScope.launch {
                            val res = commonService.authorizeOnServer(
                                accessCode,
                                Role.MODERATOR.role
                            )
                            if (res.isSuccessful) {
                                jwtToken = res.body()!!.jwtString
                            }
                        }
                        job.join()
                    }
                    Log.e("JWT", jwtToken.toString())
                } catch (e: Exception) {
                    Log.e("JWT ERROR", e.message.toString())
                }

                return liveData {
                    try {
                        val job = GlobalScope.launch(handler) {
                            val profile = commonService.getProfile(jwtToken.toString())
                            if (profile != null) {
                                repository.setProfile(profile)
                                val user = DBUserDTO(
                                    0,
                                    profile.default_email!!,
                                    "",
                                    Role.MODERATOR.toString(),
                                    jwtToken
                                )
                                repository.setOauthUser(user)
                                repository.setCurrentUser(user)
                                emit(profile)
                            }
                        }
                        job.join()
                    } catch (e: Exception) {
                        Log.e("SET PROFILE ERROR", e.message.toString())
                    }
                }
            } catch (e: Exception) {
                Log.e("SERVER", "МЫ УПАЛИ")
            }

        }
        return liveData {
            emit(repository.currentProfileInfo.value)
        }
    }
}