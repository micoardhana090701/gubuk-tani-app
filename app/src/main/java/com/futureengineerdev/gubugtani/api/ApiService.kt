package com.futureengineerdev.gubugtani.api

import com.futureengineerdev.gubugtani.request.ArticlesResponse
import com.futureengineerdev.gubugtani.request.LoginRequest
import com.futureengineerdev.gubugtani.request.RegisterRequest
import com.futureengineerdev.gubugtani.response.LoginResponse

import com.futureengineerdev.gubugtani.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(@Body requestBody: LoginRequest): Call<LoginResponse>
    @POST("auth/register")
    fun register(@Body requestBody: RegisterRequest): Call<RegisterResponse>

    @GET("article")
    suspend fun getArticles(
        @Header("Authorization") access_token: String,
    ): ArticlesResponse


}