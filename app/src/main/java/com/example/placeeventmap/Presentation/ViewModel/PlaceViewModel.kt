package com.example.placeeventmap.Presentation.ViewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class PlaceViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val userId : String = savedStateHandle["uid"] ?:
        throw IllegalArgumentException("missing user id")
//    val user : User = TODO()
}