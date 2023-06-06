package com.futureengineerdev.gubugtani.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.PlantDiseaseResultResponse
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class DiseaseResultViewModel(private val preferences: UserPreferences): ViewModel() {
//    private val _getDisease = MutableLiveData<Resource<PlantDiseaseResultResponse>>()
//    val getDisease : MutableLiveData<Resource<PlantDiseaseResultResponse>> = _getDisease
//
//    suspend fun getResultDisease(access_token: String){
//        _getDisease.postValue(Resource.Loading())
//        val accessToken = "Bearer ${preferences.getUserKey().first()}"
//        ApiConfig.apiInstance.getResultDetection(access_token = accessToken).enqueue(object : Callback<PlantDiseaseResultResponse> {
//            override fun onResponse(
//                call: Call<PlantDiseaseResultResponse>,
//                response: Response<PlantDiseaseResultResponse>
//            ) {
//                if (response.isSuccessful){
//                    val data = response.body()
//                    if(data != null){
//                        _getDisease.postValue(Resource.Success(data))
//                    }
//                }
//                else{
//                    Log.e("Error: ", "onFailure : ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<PlantDiseaseResultResponse>, t: Throwable) {
//                Log.d(ContentValues.TAG, "On failure : ${t.message}")
//            }
//
//        })
//    }
}