package com.futureengineerdev.gubugtani.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PestisideViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is pestiside Fragment"
    }
    val text: LiveData<String> = _text
}