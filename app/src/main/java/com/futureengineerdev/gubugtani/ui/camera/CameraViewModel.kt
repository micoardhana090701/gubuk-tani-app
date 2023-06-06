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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Callback
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class CameraViewModel(private val preferences: UserPreferences) : ViewModel() {

    private val _uploadDisease = MutableLiveData<Resource<PlantDiseaseResponse>>()
    val uploadDisease : LiveData<Resource<PlantDiseaseResponse>> = _uploadDisease

    private val _getDisease = MutableLiveData<Resource<Detection>>()
    val getDisease : LiveData<Resource<Detection>> = _getDisease


    suspend fun upload(
        image : MultipartBody.Part?,
        plant_id: RequestBody,
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            _uploadDisease.postValue(Resource.Loading())
            ApiConfig.apiInstance.getDetection(accessToken, image = image, plant_id = plant_id).let {
                if (Resource.Success(it) != null){
                    _uploadDisease.postValue(Resource.Success(it))
                }else{
                    _uploadDisease.postValue(Resource.Error(it.meta?.message))
                }
            }
        }
    }
    suspend fun getResultDisease(
        confidence: String,
        resultDisease: String,
    ){
        val accessToken ="Bearer ${preferences.getUserKey().first()}"
        _uploadDisease.postValue(Resource.Loading())
        ApiConfig.apiInstance.getDetectionResult(accessToken, Detection(confidence, resultDisease)).enqueue(object : Callback<PlantDiseaseResultResponse> {
            override fun onResponse(call: Call<PlantDiseaseResultResponse>, response: Response<PlantDiseaseResultResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result?.detection
                    _getDisease.postValue(Resource.Success(result))
                } else {
                    _uploadDisease.postValue(Resource.Error(response.body()?.meta?.message))
                }
            }

            override fun onFailure(call: Call<PlantDiseaseResultResponse>, t: Throwable) {
                _uploadDisease.postValue(Resource.Error(t.message))
            }
        })
    }
}