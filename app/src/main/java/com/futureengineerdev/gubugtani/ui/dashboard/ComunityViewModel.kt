package com.futureengineerdev.gubugtani.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
import kotlinx.coroutines.flow.first
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class ComunityViewModel(private val repository: ArticleRepository) : ViewModel() {

    fun getArticle(): LiveData<PagingData<ArticlesWithImages>> = repository.getArticle().cachedIn(viewModelScope)
    fun searchArticle(query: String): LiveData<ArticlesWithImages> {
        return repository.searchArticle(query)
    }
}