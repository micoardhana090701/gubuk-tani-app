package com.futureengineerdev.gubugtani.ui.profile

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.api.ApiService
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(preferences: UserPreferences) : ViewModel() {

//    private val profileRepository = ProfileRepository(preferences) // Ganti dengan repository yang sesuai dengan aplikasi Anda
//    private val _profile = MutableLiveData<Resource<UserProfile>>()
//    val profile: LiveData<Resource<UserProfile>>
//        get() = _profile
//
//    fun fetchUserProfile(accessToken: String) {
//        _profile.value = Resource.Loading()
//        profileRepository.getUserProfile(accessToken) { result ->
//            _profile.value = result
//        }
//    }
}