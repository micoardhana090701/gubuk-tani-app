package com.futureengineerdev.gubugtani.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.ChoosingPlantResponse
import com.futureengineerdev.gubugtani.response.PaymentMethodResponse
import kotlinx.coroutines.launch

class PaymentMethodViewModel(private val preferences: UserPreferences): ViewModel() {
    private val _method = MutableLiveData<PaymentMethodResponse>()
    val method : MutableLiveData<PaymentMethodResponse> = _method

    suspend fun getPaymentMethod(access_token: String){
        viewModelScope.launch {
            ApiConfig.apiInstance.getPaymentMethod(access_token).let {
                _method.postValue(it)
            }
        }
    }
}