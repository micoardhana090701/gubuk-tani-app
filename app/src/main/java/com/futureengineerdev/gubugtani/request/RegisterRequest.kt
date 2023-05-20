package com.futureengineerdev.gubugtani.request

data class RegisterRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
)