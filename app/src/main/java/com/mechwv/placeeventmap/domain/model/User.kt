package com.mechwv.placeeventmap.domain.model

import com.mechwv.placeeventmap.presentation.retrofit.model.idApi.OauthToken

open class User(
    open var id: Int = 0,
    open var email: String,
    open var hashed_pass: String,
    open var role: String,
    open var oauthToken: String? = null
)
