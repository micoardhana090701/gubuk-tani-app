package com.futureengineerdev.gubugtani.api

import com.futureengineerdev.gubugtani.request.ArticlesResponse
import com.futureengineerdev.gubugtani.request.LoginRequest
import com.futureengineerdev.gubugtani.request.RegisterRequest
import com.futureengineerdev.gubugtani.response.LoginResponse
import com.futureengineerdev.gubugtani.response.ProfileResponse

import com.futureengineerdev.gubugtani.response.RegisterResponse
import com.futureengineerdev.gubugtani.response.UpdateMeta
import com.futureengineerdev.gubugtani.response.UpdateResponse
import com.futureengineerdev.gubugtani.response.UpdateUser
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(@Body requestBody: LoginRequest): Call<LoginResponse>
    @POST("auth/register")
    fun register(@Body requestBody: RegisterRequest): Call<RegisterResponse>
    @GET("profile")
    fun getProfile(
        @Header("Authorization") access_token: String,
    ): Call<ProfileResponse>

    @Multipart
    @POST("profile")
    suspend fun updateAll(
        @Header("Authorization") access_token: String,
        @Part avatar: MultipartBody.Part?,
        @Part("username") username: RequestBody,
        @Part("name") name: RequestBody,
        @Part("city") city: RequestBody,
    ): UpdateResponse

    @Multipart
    @POST("profile")
    suspend fun updateData(
        @Header("Authorization") access_token: String,
        @Part("username") username: RequestBody,
        @Part("name") name: RequestBody,
        @Part("city") city: RequestBody,
    ): UpdateResponse

    @GET("article")
    suspend fun getArticles(
        @Header("Authorization") access_token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 1,
    ): ArticlesResponse

}