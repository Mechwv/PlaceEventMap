package com.mechwv.placeeventmap.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import com.mechwv.placeeventmap.domain.model.Role
import com.mechwv.placeeventmap.domain.model.User
import com.mechwv.placeeventmap.presentation.retrofit.Common
import com.mechwv.placeeventmap.presentation.room.UserRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.dto.DBUserDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepositoryImpl
): ViewModel() {
    private val authService = Common

    fun setCurrentUser(user: User) {
        repository.setCurrentUser(user)
    }

    fun setAdmin() {
        val admin = DBUserDTO(0, "qwazar", "123123123", Role.ADMIN.toString())
        repository.setOauthUser(admin)
    }

    fun getUser(email: String, password: String): LiveData<User> {
        return repository.getUser(email, password)
    }

    fun getOauthUser(): LiveData<User> {
        return repository.getOauthUser()
    }

    fun getProfileByToken(token: String): LiveData<ProfileInfo?> {
        try {
            return liveData {
                emit(authService.getProfile(token))
            }
        } catch (e: Exception) {
            return liveData {
                emit(null)
            }
        }
    }

    fun getProfile(): LiveData<ProfileInfo?> {
        return repository.currentProfileInfo
    }

    fun setProfile(profile: ProfileInfo){
        return repository.setProfile(profile)
    }
}