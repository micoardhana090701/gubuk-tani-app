package com.futureengineerdev.gubugtani.ui.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.ProfileResponse
import com.futureengineerdev.gubugtani.response.UpdateResponse
import com.futureengineerdev.gubugtani.response.UpdateUser
import kotlinx.coroutines.flow.first
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val preferences: UserPreferences) : ViewModel() {

    private val _profileUser = MutableLiveData<ProfileResponse>()
    val profileUser : LiveData<ProfileResponse> = _profileUser

    private val _updateUser = MutableLiveData<UpdateResponse>()
    val updateUser : LiveData<UpdateResponse> = _updateUser

    fun getProfile(access_token: String){
        ApiConfig.apiInstance.getProfile(access_token).enqueue(object : Callback<ProfileResponse>{
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        _profileUser.postValue(data!!)
                    }
                }else{
                    Log.e("Error: ", "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })

    }

    suspend fun updateProfile(city: String, name: String, email: String, username: String){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        ApiConfig.apiInstance.updateProfile(accessToken, UpdateUser(city, name, email, username)).enqueue(object : Callback<UpdateResponse>{

            override fun onResponse(
                call: Call<UpdateResponse>,
                response: Response<UpdateResponse>
            ) {
                if (response.isSuccessful){
                    val data = response.body()
                    if (data != null){
                        _updateUser.postValue(data!!)
                    }
                }else{
                    Log.e("Error: ", "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message}")
            }

        })

    }

}