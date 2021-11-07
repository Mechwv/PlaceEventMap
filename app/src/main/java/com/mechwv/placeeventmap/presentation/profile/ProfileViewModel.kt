package com.mechwv.placeeventmap.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import com.mechwv.placeeventmap.presentation.room.UserRepositoryImpl
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
}