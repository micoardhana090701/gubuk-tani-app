package com.futureengineerdev.gubugtani.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.GetCommentResponse
import kotlinx.coroutines.launch

class GetCommentViewModel(private val preferences: UserPreferences): ViewModel() {
    private val _comment = MutableLiveData<GetCommentResponse>()
    val comment : LiveData<GetCommentResponse> = _comment

    suspend fun getComment(accessToken: String, articleId: Int){
        viewModelScope.launch {
            ApiConfig.apiInstance.getComment(accessToken, articleId).let {
                _comment.postValue(it)
            }
        }
    }

}