package com.futureengineerdev.gubugtani.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.HistoryResponse
import kotlinx.coroutines.launch

class HistoryViewModel(private val preferences: UserPreferences): ViewModel(){
    private val _history = MutableLiveData<HistoryResponse>()
    val history : LiveData<HistoryResponse> = _history

    suspend fun getHistory(accessToken: String, user_id: Int){
        viewModelScope.launch {
            ApiConfig.apiInstance.getHistory(accessToken, user_id = user_id).let {
                _history.postValue(it)
            }
        }
    }
}