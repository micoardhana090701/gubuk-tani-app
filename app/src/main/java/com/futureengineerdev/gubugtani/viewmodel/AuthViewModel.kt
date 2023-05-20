package com.futureengineerdev.gubugtani.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.request.LoginRequest
import com.futureengineerdev.gubugtani.response.BaseResponse
import com.futureengineerdev.gubugtani.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel (private val preferences: UserPreferences):ViewModel() {

    private val _authInfo = MutableLiveData<Resource<String>>()
    val authInfo: LiveData<Resource<String>> = _authInfo

    fun login(email: String, password: String) {
        _authInfo.postValue(Resource.Loading())
        val client = ApiConfig.apiInstance.login(LoginRequest(email, password))

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result?.access_token

                    result?.let { saveUserKey(it) }
                    _authInfo.postValue(Resource.Success(result))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _authInfo.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _authInfo.postValue(Resource.Error(t.message))
            }
        })
    }

    fun logout() = deleteUserKey()

    fun getUserKey() = preferences.getUserKey().asLiveData()

    private fun saveUserKey(key: String) {
        viewModelScope.launch {
            preferences.saveUserKey(key)
        }
    }

    private fun deleteUserKey() {
        viewModelScope.launch {
            preferences.deleteUserKey()
        }
    }
}