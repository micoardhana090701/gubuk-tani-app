package com.futureengineerdev.gubugtani.ui.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.ProfileResponse
import com.futureengineerdev.gubugtani.response.UpdateResponse
import com.futureengineerdev.gubugtani.response.UpdateUser
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val preferences: UserPreferences) : ViewModel() {

    private val _profileUser = MutableLiveData<Resource<ProfileResponse>>()
    val profileUser : LiveData<Resource<ProfileResponse>> = _profileUser

    private val _updateUser = MutableLiveData<UpdateResponse>()
    val updateUser : LiveData<UpdateResponse> = _updateUser

    fun getProfile(access_token: String){
        _profileUser.postValue(Resource.Loading())
        ApiConfig.apiInstance.getProfile(access_token).enqueue(object : Callback<ProfileResponse>{
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        _profileUser.postValue(Resource.Success(data))
                    }
                }
                else{
                    Log.e("Error: ", "onFailure : ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })

    }

    suspend fun updateAll(
        imageMultipart: MultipartBody.Part,
        username: RequestBody,
        name: RequestBody,
        city: RequestBody
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            ApiConfig.apiInstance.updateAll(accessToken, imageMultipart, username=username, name=name, city=city).let {
                _updateUser.postValue(it)
            }
        }
    }
    suspend fun updateData(
        username: RequestBody,
        name: RequestBody,
        city: RequestBody
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            ApiConfig.apiInstance.updateData(accessToken, username=username, name=name, city=city).let {
                _updateUser.postValue(it)
            }
        }
    }
}