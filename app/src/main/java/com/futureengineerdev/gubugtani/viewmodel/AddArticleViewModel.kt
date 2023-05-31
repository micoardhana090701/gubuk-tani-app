package com.futureengineerdev.gubugtani.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.UpdateResponse
import com.futureengineerdev.gubugtani.response.UploadArticleResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart

class AddArticleViewModel(private val preferences: UserPreferences): ViewModel() {
    private val _upload = MutableLiveData<UploadArticleResponse>()
    val upload : LiveData<UploadArticleResponse> = _upload
    private val _imageMultipart = mutableListOf<MultipartBody.Part>()
    val imageMultipart = _imageMultipart

    suspend fun upload(
        images0 : MultipartBody.Part?,
        type: RequestBody,
        title : RequestBody,
        content: RequestBody,
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            ApiConfig.apiInstance.uploadArticle(accessToken, images0 = images0, type=type, title=title, content=content).let {
                _upload.postValue(it)
            }
        }
    }
}