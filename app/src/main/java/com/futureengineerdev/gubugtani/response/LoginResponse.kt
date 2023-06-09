package com.futureengineerdev.gubugtani.response

data class LoginResponse(
    val meta: MetaDataLogin,
    val result: LoginResult
)
data class MetaDataLogin(
    val code: Int,
    val status: String,
    val message: String
)

data class LoginResult(
    val access_token: String,
    val token_type: String,
    val user: UserLogin
)

data class UserLogin(
    val id: Int,
    val name: String,
    val email: String,
    val username: String,
    val city: String?,
    val avatar: String?,
    val role: String,
    val email_verified_at: String?,
    val two_factor_confirmed_at: String?,
    val current_team_id: String?,
    val profile_photo_path: String?,
    val created_at: String,
    val updated_at: String,
    val profile_photo_url: String
)