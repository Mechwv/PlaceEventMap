package com.mechwv.placeeventmap.presentation.moderator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.domain.model.User
import com.mechwv.placeeventmap.presentation.retrofit.Common
import com.mechwv.placeeventmap.presentation.room.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModeratorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepositoryImpl
) : ViewModel() {
    private val commonService = Common

    fun getModeratorPlaces(jwtToken : String): LiveData<List<Place>> {
        return liveData {
            val places = commonService.getPlaces(jwtToken)
            Log.e("PLACES", places.toString())
            emit(places)
        }
    }
    // TODO: Implement the ViewModel
}