package com.mechwv.placeeventmap.domain.model

data class ProfileInfo(
    val birthday: String? = null,
    val client_id: String?= null,
    val default_avatar_id: String?= null,
    val default_email: String? = null,
    val display_name: String? = null,
    val emails: List<String>? = null,
    val first_name: String? = null,
    val id: String? = null,
    val is_avatar_empty: Boolean? = null,
    val last_name: String? = null,
    val login: String? = null,
    val old_social_login: String? = null,
    val openid_identities: List<String>? = null,
    val psuid: String? = null,
    val real_name: String? = null,
    val sex: String? = null
)