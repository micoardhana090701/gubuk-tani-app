package com.futureengineerdev.gubugtani.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.futureengineerdev.gubugtani.etc.UserPreferences

class ViewModelFactory(private val pref: UserPreferences): ViewModelProvider.NewInstanceFactory() {
    private lateinit var _Application: Application

    fun setApplication(application: Application){
        _Application = application
    }

    @Suppress("UNCHECKED_CAST")
    override fun<T: ViewModel> create(modelClass: Class<T>): T{
        return when(modelClass){
            AuthViewModel::class.java -> AuthViewModel(pref) as T
            else -> throw  IllegalAccessException("Unknown ViewModel class: "+ modelClass.name)
        }
    }
}