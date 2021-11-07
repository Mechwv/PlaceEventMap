package com.mechwv.placeeventmap.presentation.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mechwv.placeeventmap.domain.model.Place
import com.mechwv.placeeventmap.domain.model.Role
import com.mechwv.placeeventmap.domain.model.User

@Entity(tableName = "app_users")
data class DBUserDTO(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "email") override var email: String,
    @ColumnInfo(name = "hashed_pass") override var hashed_pass: String,
    @ColumnInfo(name = "role") override var role: String,
    @ColumnInfo(name = "oauth_token") override var oauthToken: String? = null,
): User(uid, email, hashed_pass, role, oauthToken) {

    constructor(user: User) : this(
        user.id,
        user.email,
        user.hashed_pass,
        user.role,
        user.oauthToken
    )
}
