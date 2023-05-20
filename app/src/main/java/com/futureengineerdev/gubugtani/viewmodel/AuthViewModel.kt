package com.futureengineerdev.gubugtani.viewmodel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.preferences.protobuf.Api
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.api.ApiService
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.request.LoginRequest
import com.futureengineerdev.gubugtani.request.RegisterRequest
import com.futureengineerdev.gubugtani.response.ErrorResponse
import com.futureengineerdev.gubugtani.response.LoginResponse
import com.futureengineerdev.gubugtani.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AuthViewModel(private val preferences: UserPreferences):ViewModel() {

    private val _authInfo = MutableLiveData<Resource<String>>()
    val authInfo: LiveData<Resource<String>> = _authInfo

    fun loginUser(email: String, password: String){
        _authInfo.postValue(Resource.Loading())
        val client = ApiConfig.apiInstance.login(LoginRequest(email, password))

        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResult = response.body()?.result?.access_token
                    loginResult?.let { saveUserKey(it) }
                    _authInfo.postValue(Resource.Success(loginResult))
                }else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        LoginResponse::class.java
                    )
                    _authInfo.postValue(Resource.Error(errorResponse.meta.message))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _authInfo.postValue(Resource.Error(t.message))
            }

        })
    }
    fun registerUser(name: String, username: String, email: String, password: String){
        _authInfo.postValue(Resource.Loading())
        val client = ApiConfig.apiInstance.register(RegisterRequest(name, username, email, password))

        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful){
                    val registerResponse = response.body()?.meta?.message
                    _authInfo.postValue(Resource.Success(registerResponse))
                }else{
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    if (errorResponse != null){
                        val usernameError = errorResponse.errors.username
                        val emailError = errorResponse.errors.email
                        if (usernameError != null){
                            for (error in usernameError){
                                Log.e("RegisterActivity", "Username Error: $error")
                            }
                        }
                        if (emailError != null){
                            for (error in emailError){
                                Log.e("RegisterActivity", "Email Error: $error")
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _authInfo.postValue(Resource.Error(t.message))
            }
        })
    }
    fun getUserKey() = preferences.getUserKey().asLiveData()

    private fun saveUserKey(key: String) {
        viewModelScope.launch {
            preferences.saveUserKey(key)
        }
    }
}