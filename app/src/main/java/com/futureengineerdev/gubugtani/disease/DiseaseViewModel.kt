package com.futureengineerdev.gubugtani.disease

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.DiseaseResponse
import kotlinx.coroutines.launch

class DiseaseViewModel(private val preferences: UserPreferences): ViewModel() {

    private val _disease = MutableLiveData<DiseaseResponse>()
    val disease: MutableLiveData<DiseaseResponse> = _disease

    suspend fun getDisease(access_token: String) {
        viewModelScope.launch {
            ApiConfig.apiInstance.getDisease(access_token).let {
                _disease.postValue(it)
            }
        }
    }

}