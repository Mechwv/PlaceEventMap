package com.mechwv.placeeventmap.presentation.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.mechwv.placeeventmap.presentation.room.BaseDao
import com.mechwv.placeeventmap.presentation.room.dto.DBEventDTO
import com.mechwv.placeeventmap.presentation.room.dto.DBUserDTO

@Dao
abstract class  UserDao : BaseDao<DBUserDTO> {

    @Query("SELECT * FROM app_users where email = :email and hashed_pass = :password")
    abstract fun get(email: String, password: String): LiveData<DBUserDTO>
}