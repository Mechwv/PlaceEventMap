package com.mechwv.placeeventmap.presentation.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mechwv.placeeventmap.data.repository.UserRepository
import com.mechwv.placeeventmap.domain.model.ProfileInfo
import com.mechwv.placeeventmap.domain.model.User
import com.mechwv.placeeventmap.presentation.room.dao.UserDao
import com.mechwv.placeeventmap.presentation.room.dto.DBUserDTO
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepositoryImpl
constructor(
    private val userDao: UserDao
) : UserRepository
{
    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val _currentProfileInfo = MutableLiveData<ProfileInfo?>()
    val currentProfileInfo: LiveData<ProfileInfo?>
        get() = _currentProfileInfo

    fun setProfile(profileInfo: ProfileInfo) {
        _currentProfileInfo.value = profileInfo
    }

    override fun setUser(userDTO: DBUserDTO) {
       executorService.execute {
           userDao.insert(userDTO)
       }
    }

    override fun getUser(email: String, password: String): LiveData<User> {
        return userDao.get(email, password) as LiveData<User>
    }
}