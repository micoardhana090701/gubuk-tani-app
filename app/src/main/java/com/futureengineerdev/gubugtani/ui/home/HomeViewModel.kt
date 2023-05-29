package com.futureengineerdev.gubugtani.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.futureengineerdev.gubugtani.article.ArticleRepository
import com.futureengineerdev.gubugtani.database.ArticlesWithImages

class HomeViewModel(private val repository: ArticleRepository) : ViewModel() {

    fun getArticle(): LiveData<PagingData<ArticlesWithImages>> = repository.getArticle().cachedIn(viewModelScope)
}