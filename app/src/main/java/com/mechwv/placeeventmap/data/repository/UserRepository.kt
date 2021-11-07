package com.mechwv.placeeventmap.data.repository

import androidx.lifecycle.LiveData
import com.mechwv.placeeventmap.domain.model.User
import com.mechwv.placeeventmap.presentation.room.dto.DBUserDTO

interface UserRepository {
    fun setOauthUser(userDTO: DBUserDTO)
    fun getUser(email: String, password: String): LiveData<User>
    fun getOauthUser(): LiveData<User>
    fun deleteUser()
}