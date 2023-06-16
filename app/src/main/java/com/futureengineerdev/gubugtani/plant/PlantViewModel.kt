package com.futureengineerdev.gubugtani.plant

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.ChoosingPlantResponse
import com.futureengineerdev.gubugtani.response.PlantsItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class PlantViewModel(private val preferences: UserPreferences) : ViewModel() {

    private val _plant = MutableLiveData<ChoosingPlantResponse>()
    val plant : MutableLiveData<ChoosingPlantResponse> = _plant

    suspend fun getPlant(access_token: String){
        viewModelScope.launch {
            ApiConfig.apiInstance.choosingPlant(access_token).let {
                if (it !=null){
                    _plant.postValue(it)
                }
                else{
                    Log.e("Tidak Ada data", "not found")
                }
            }
        }
    }

}