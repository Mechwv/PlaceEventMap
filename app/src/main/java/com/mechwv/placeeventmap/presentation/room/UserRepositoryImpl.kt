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

    private val _currentUserInfo = MutableLiveData<User?>()
    val currentUserInfo: LiveData<User?>
        get() = _currentUserInfo

    fun setCurrentUser(user: User?) {
        _currentUserInfo.value = user
    }

    fun setProfile(profileInfo: ProfileInfo?) {
        _currentProfileInfo.value = profileInfo
    }

    override fun setOauthUser(userDTO: DBUserDTO) {
       executorService.execute {
           userDao.setOauthUser(userDTO)
       }
    }

    override fun getUser(email: String, password: String): LiveData<User> {
        return userDao.getBaseUser(email, password) as LiveData<User>
    }

    override fun getOauthUser(): LiveData<User> {
        return userDao.getOauthUser() as LiveData<User>
    }

    override fun deleteUser() {
        executorService.execute {
            userDao.deleteOauthUser()
        }
    }
}