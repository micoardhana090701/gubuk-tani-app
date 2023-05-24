package com.futureengineerdev.gubugtani.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.futureengineerdev.gubugtani.article.ArticleRepository
import com.futureengineerdev.gubugtani.database.Articles
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.request.DataItem

class ComunityViewModel(private val repository: ArticleRepository) : ViewModel() {
    fun getArticle(): LiveData<PagingData<Articles>> =
        repository.getArticle().cachedIn(viewModelScope)
}