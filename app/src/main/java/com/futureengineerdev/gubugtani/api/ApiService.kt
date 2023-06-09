package com.futureengineerdev.gubugtani.api

import com.futureengineerdev.gubugtani.article.ArticlesResponse
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.request.LoginRequest
import com.futureengineerdev.gubugtani.request.RegisterRequest
import com.futureengineerdev.gubugtani.response.AddCommentResponse
import com.futureengineerdev.gubugtani.response.ChoosingPlantResponse
import com.futureengineerdev.gubugtani.response.CommentAddComment
import com.futureengineerdev.gubugtani.response.Detection
import com.futureengineerdev.gubugtani.response.DiseaseResponse
import com.futureengineerdev.gubugtani.response.GetCommentResponse
import com.futureengineerdev.gubugtani.response.HistoryResponse
import com.futureengineerdev.gubugtani.response.LoginResponse
import com.futureengineerdev.gubugtani.response.PaymentMethodResponse
import com.futureengineerdev.gubugtani.response.PaymentResponse
import com.futureengineerdev.gubugtani.response.PlantDiseaseResponse
import com.futureengineerdev.gubugtani.response.PlantDiseaseResult
import com.futureengineerdev.gubugtani.response.PlantDiseaseResultResponse
import com.futureengineerdev.gubugtani.response.ProfileResponse
import com.futureengineerdev.gubugtani.response.RegisterResponse
import com.futureengineerdev.gubugtani.response.UpdateResponse
import com.futureengineerdev.gubugtani.response.UploadArticleResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
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
        @Query("type") type: Query? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int = 5,
        @Query("limit") limit: Int = 5,
    ): ArticlesResponse

    @Multipart
    @POST("article")
    suspend fun uploadArticle(
        @Header("Authorization") access_token: String,
        @Part images0 : MultipartBody.Part?,
        @Part("type") type: RequestBody,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
    ): UploadArticleResponse

    @GET("plant")
    suspend fun choosingPlant(
        @Header("Authorization") access_token: String,
    ): ChoosingPlantResponse

    @Multipart
    @POST("detection")
    suspend fun getDetection(
        @Header("Authorization") access_token: String,
        @Part image : MultipartBody.Part?,
        @Part("plant_id") plant_id: RequestBody,
    ): Response<PlantDiseaseResponse>

    @GET("disease")
    suspend fun getDisease(
        @Header("Authorization") access_token: String,
        @Query("tag") tag: String? = null,
    ): DiseaseResponse

    @Multipart
    @POST("article/{article_id}/comment")
    suspend fun addComment(
        @Header("Authorization") access_token: String,
        @Path("article_id") articleId: Int,
        @Part("comment") comment: RequestBody,
    ): AddCommentResponse

    @GET("article/{article_id}/comment")
    suspend fun getComment(
        @Header("Authorization") access_token: String,
        @Path("article_id") articleId: Int,
    ): GetCommentResponse

    @GET("detection")
    suspend fun getHistory(
        @Header("Authorization") access_token: String,
        @Query("user_id") user_id: Int,
    ): HistoryResponse

    @GET("payment_method")
    suspend fun getPaymentMethod(
        @Header("Authorization") access_token: String,
    ): PaymentMethodResponse

    @Multipart
    @POST("payment")
    suspend fun uploadPayment(
        @Header("Authorization") access_token: String,
        @Part image : MultipartBody.Part?,
        @Part("payment_method_id") paymentMethodId: RequestBody,
        @Part("notes") notes: RequestBody,
    ): Response<PaymentResponse>
}