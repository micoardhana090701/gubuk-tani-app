package com.futureengineerdev.gubugtani.response

data class RegisterResponse(
    val meta: MetaData,
    val result: RegisterResult
)
data class MetaData(
    val code: Int,
    val status: String,
    val message: String
)

data class RegisterResult(
    val access_token: String,
    val token_type: String,
    val user: User
)

data class User(
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