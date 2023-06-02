package com.futureengineerdev.gubugtani.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.util.query
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.article.ArticleRepository
import com.futureengineerdev.gubugtani.article.ArticlesResponse
import com.futureengineerdev.gubugtani.article.Result
import com.futureengineerdev.gubugtani.database.ArticleImages
import com.futureengineerdev.gubugtani.database.Articles
import com.futureengineerdev.gubugtani.database.ArticlesResponseData
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class ComunityViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val _articlesLiveData = MutableLiveData<PagingData<ArticlesWithImages>>()
    val articlesLiveData: LiveData<PagingData<ArticlesWithImages>> = _articlesLiveData
    fun getArticle(searchQuery: String): LiveData<PagingData<ArticlesWithImages>> = repository.getArticle(searchQuery).cachedIn(viewModelScope)


}