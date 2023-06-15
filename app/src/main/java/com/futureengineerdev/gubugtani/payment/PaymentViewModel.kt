package com.futureengineerdev.gubugtani.payment

import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.PaymentResponse
import com.futureengineerdev.gubugtani.response.PlantDiseaseResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart

class PaymentViewModel(private val preferences: UserPreferences): ViewModel() {

    private val _uploadPayment = MutableLiveData<PaymentResponse>()
    val uploadPayment : LiveData<PaymentResponse> = _uploadPayment

    suspend fun upload(
        image: MultipartBody.Part?,
        paymentMethodId: RequestBody,
        notes: RequestBody
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            ApiConfig.apiInstance.uploadPayment(accessToken, image= image, paymentMethodId = paymentMethodId, notes = notes).let { response ->
                if (response.isSuccessful){
                    if (response.body() != null){
                        val result = response.body()
                        _uploadPayment.postValue(result!!)
                    }
                }
                else if (response.code() == 500){
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        PaymentResponse::class.java
                    )
                    _uploadPayment.postValue(errorResponse)
                }
                else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        PaymentResponse::class.java
                    )
                    _uploadPayment.postValue(errorResponse)
                }
            }
        }
    }
}