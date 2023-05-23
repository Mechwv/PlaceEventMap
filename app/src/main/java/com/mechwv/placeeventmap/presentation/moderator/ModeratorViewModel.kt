package com.mechwv.placeeventmap.presentation.moderator

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.presentation.retrofit.Common
import com.mechwv.placeeventmap.presentation.room.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModeratorViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UserRepositoryImpl
) : ViewModel() {
    private val commonService = Common

    fun getModeratorPlaces(jwtToken: String, context: Context?): LiveData<List<Place>> {

        val handler = CoroutineExceptionHandler { _, exception ->
            println(exception)
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Nothing to check or there is no internet",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        return liveData {
            val job = GlobalScope.launch(handler) {
                val places = commonService.getModeratorPlaces(jwtToken)
                Log.e("PLACES", places.toString())
                emit(places)
            }
            job.join()
        }
    }
    // TODO: Implement the ViewModel
}