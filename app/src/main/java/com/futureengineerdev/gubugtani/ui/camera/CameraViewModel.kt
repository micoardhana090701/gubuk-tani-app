package com.futureengineerdev.gubugtani.ui.camera

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.request.RegisterRequest
import com.futureengineerdev.gubugtani.response.ChoosingPlantResponse
import com.futureengineerdev.gubugtani.response.Detection
import com.futureengineerdev.gubugtani.response.PlantDiseaseResponse
import com.futureengineerdev.gubugtani.response.PlantDiseaseResult
import com.futureengineerdev.gubugtani.response.PlantDiseaseResultResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Callback
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class CameraViewModel(private val preferences: UserPreferences) : ViewModel() {

    private val _uploadDisease = MutableLiveData<PlantDiseaseResponse>()
    val uploadDisease : LiveData<PlantDiseaseResponse> = _uploadDisease

    private val _getDisease = MutableLiveData<Resource<Detection>>()
    val getDisease : LiveData<Resource<Detection>> = _getDisease


    suspend fun upload(
        image : MultipartBody.Part?,
        plant_id: RequestBody,
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            ApiConfig.apiInstance.getDetection(accessToken, image = image, plant_id = plant_id).let {response ->
                if (response.isSuccessful){
                    if (response.body() != null){
                        val result = response.body()
                        _uploadDisease.postValue(result!!)
                    }
                }
                else if (response.code() == 400){
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        PlantDiseaseResponse::class.java
                    )
                    _uploadDisease.postValue(errorResponse)
                }
                else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        PlantDiseaseResponse::class.java
                    )
                    _uploadDisease.postValue(errorResponse)
                }
            }
        }
    }

}