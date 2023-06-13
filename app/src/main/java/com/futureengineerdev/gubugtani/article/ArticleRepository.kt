package com.futureengineerdev.gubugtani.article

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.futureengineerdev.gubugtani.api.ApiService
import com.futureengineerdev.gubugtani.database.ArticleImages
import com.futureengineerdev.gubugtani.database.Articles
import com.futureengineerdev.gubugtani.database.ArticlesDatabase
import com.futureengineerdev.gubugtani.database.ArticlesWithImages
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepository (private val articlesDatabase: ArticlesDatabase, private val apiService: ApiService, private val access_token: UserPreferences){

    fun getArticle(searchQuery: String) : LiveData<PagingData<ArticlesWithImages>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 7
            ),
            remoteMediator = ArticlesRemoteMediator(articlesDatabase, apiService, access_token=access_token,  searchQuery = searchQuery),
            pagingSourceFactory = {
                articlesDatabase.articlesDao().findAll()
            }
        ).liveData
    }
}