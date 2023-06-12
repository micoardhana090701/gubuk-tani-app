package com.futureengineerdev.gubugtani.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.AddCommentResponse
import com.futureengineerdev.gubugtani.response.CommentAddComment
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class CommentViewModel (private val preferences: UserPreferences): ViewModel(){

}