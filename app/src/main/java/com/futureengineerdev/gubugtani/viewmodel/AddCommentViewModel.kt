package com.futureengineerdev.gubugtani.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.AddCommentResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class AddCommentViewModel(private val preferences: UserPreferences): ViewModel() {
    private val _comment = MutableLiveData<Resource<AddCommentResponse>>()
    val comment : LiveData<Resource<AddCommentResponse>> = _comment

    suspend fun addComment(
        article_id: Int,
        comment: RequestBody,
    ){
        val accessToken = "Bearer ${preferences.getUserKey().first()}"
        viewModelScope.launch {
            _comment.postValue(Resource.Loading())
            ApiConfig.apiInstance.addComment(accessToken, articleId = article_id, comment = comment).let {
                if (Resource.Success(it) != null){
                    _comment.postValue(Resource.Success(it))
                }else{
                    _comment.postValue(Resource.Error(it.meta?.message))
                }
            }
        }
    }
}