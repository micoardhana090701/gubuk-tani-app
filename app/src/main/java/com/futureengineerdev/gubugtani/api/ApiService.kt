package com.futureengineerdev.gubugtani.api

import com.futureengineerdev.gubugtani.LoginActivity
import com.futureengineerdev.gubugtani.request.LoginRequest
import com.futureengineerdev.gubugtani.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body requestBody: LoginRequest): Call<LoginResponse>
}