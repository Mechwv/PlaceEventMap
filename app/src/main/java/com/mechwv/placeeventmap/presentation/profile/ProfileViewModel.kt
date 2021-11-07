package com.mechwv.placeeventmap.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import com.mechwv.placeeventmap.domain.model.User
import com.mechwv.placeeventmap.presentation.room.UserRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.dto.DBUserDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepositoryImpl
): ViewModel() {

    fun getProfile(): LiveData<ProfileInfo?> {
        return repository.currentProfileInfo
    }

    fun getCurrentUser(): LiveData<User?> {
        return repository.currentUserInfo
    }

    fun logout() {
        repository.deleteUser()
        repository.setProfile(null)
        repository.setCurrentUser(null)
    }
}