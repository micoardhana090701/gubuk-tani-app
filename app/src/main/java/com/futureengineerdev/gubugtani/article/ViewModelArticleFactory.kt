package com.futureengineerdev.gubugtani.article

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.futureengineerdev.gubugtani.etc.Injection
import com.futureengineerdev.gubugtani.ui.dashboard.ComunityFragment
import com.futureengineerdev.gubugtani.ui.dashboard.ComunityViewModel
import com.futureengineerdev.gubugtani.ui.home.HomeViewModel

class ViewModelArticleFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComunityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ComunityViewModel(Injection.provideRepository(context)) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}