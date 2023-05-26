package com.futureengineerdev.gubugtani.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.futureengineerdev.gubugtani.article.ArticleRepository
import com.futureengineerdev.gubugtani.database.ArticleImages
import com.futureengineerdev.gubugtani.database.Articles
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences

class ComunityViewModel(private val repository: ArticleRepository) : ViewModel() {

    fun getArticle(): LiveData<PagingData<ArticlesWithImages>> = repository.getArticle().cachedIn(viewModelScope).asLiveData()
    fun getArticleImages(): LiveData<List<ArticlesWithImages>> = repository.getArticleImages()

}