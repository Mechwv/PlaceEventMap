package com.mechwv.placeeventmap.presentation.profile

import android.util.Log
import androidx.lifecycle.*
import com.mechwv.placeeventmap.data.repository.PlaceRepository
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import com.mechwv.placeeventmap.domain.model.User
import com.mechwv.placeeventmap.presentation.retrofit.Common
import com.mechwv.placeeventmap.presentation.room.PlaceRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.UserRepositoryImpl
import com.mechwv.placeeventmap.presentation.room.dto.DBUserDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepositoryImpl,
    private val placesRepository: PlaceRepositoryImpl
): ViewModel() {
    private val commonService = Common

    fun getProfile(): LiveData<ProfileInfo?> {
        return repository.currentProfileInfo
    }

    fun saveCurrentPlaces(jwt_token: String, places: List<Place>): LiveData<Boolean> {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        return liveData {
            try {
                val job = CoroutineScope(handler).launch {
                    val result = commonService.saveCurrentPlaces(jwt_token, places)
                    Log.e("RESULT", result.toString())
                    emit(result)
                }
                job.join()
            } catch (e: Exception) {
                emit(false)
            }
        }
    }

    fun getOnlinePlaces(jwt_token: String): LiveData<List<Place>> {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("CoroutineExceptionHandler got $exception")
        }
        return liveData {
            val job = CoroutineScope(handler).launch {
                try {
                    val result = commonService.getOnlinePlaces(jwt_token)
                    placesRepository.updateWithDownloadedValues(result)
                    Log.e("RESULT", result.toString())
                    emit(result)
                } catch (e: Exception) {
                    emit(listOf())
                }

            }
            job.join()
        }
    }

    fun getDBPlaces(filter: String = "cringe"): LiveData<List<Place>> {
        val allPlaces = placesRepository.getPlaces()
        val fp = Transformations.switchMap(allPlaces) { placeList ->
            val filteredPlaces = MutableLiveData<List<Place>>()
            val filteredList = placeList.filter { p-> p.name.contains(filter, ignoreCase = true) }
            filteredPlaces.value = filteredList
            filteredPlaces
        }
        return fp
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