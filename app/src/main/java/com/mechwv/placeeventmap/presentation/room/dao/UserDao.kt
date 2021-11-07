package com.mechwv.placeeventmap.presentation.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mechwv.placeeventmap.domain.model.User
import com.mechwv.placeeventmap.presentation.retrofit.model.idApi.OauthToken
import com.mechwv.placeeventmap.presentation.room.BaseDao
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import com.mechwv.placeeventmap.presentation.room.dto.DBUserDTO

@Dao
abstract class  UserDao : BaseDao<DBUserDTO> {

    @Query("SELECT * FROM app_users where email = :email and hashed_pass = :password")
    abstract fun getBaseUser(email: String, password: String): LiveData<DBUserDTO>

    @Query("Select * from app_users where oauth_token IS NOT NULL")
    abstract fun getOauthUser(): LiveData<DBUserDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun setOauthUser(user: DBUserDTO)

    @Query("DELETE from app_users where oauth_token IS NOT NULL")
    abstract fun deleteOauthUser()
}