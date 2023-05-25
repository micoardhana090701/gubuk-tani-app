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