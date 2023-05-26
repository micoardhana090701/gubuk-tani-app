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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepository (private val articlesDatabase: ArticlesDatabase, private val apiService: ApiService, private val access_token: UserPreferences){

//    fun getArticle() : LiveData<PagingData<Articles>> {
//        @OptIn(ExperimentalPagingApi::class)
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            remoteMediator = ArticlesRemoteMediator(articlesDatabase, apiService, access_token),
//            pagingSourceFactory = {
//                articlesDatabase.articlesDao().findAll()
//            }
//        ).liveData
//    }

    @OptIn(ExperimentalPagingApi::class)
    fun getArticle(): Flow<PagingData<ArticlesWithImages>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = ArticlesRemoteMediator(articlesDatabase, apiService, access_token),
            pagingSourceFactory = { articlesDatabase.articlesDao().findAll() }
        ).flow.map { pagingData ->
            pagingData.map { articles ->
                ArticlesWithImages(
                    articles = articles,
                    articleImages = articles.article_images.imageList
                )
            }
        }
    }
    fun getArticleImages(): LiveData<List<ArticlesWithImages>> {
        return articlesDatabase.articleImagesDao().findAll()
    }

//    fun getArticleImage() : LiveData<PagingData<ArticlesWithImages>> {
//        @OptIn(ExperimentalPagingApi::class)
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            remoteMediator = ArticlesRemoteMediator(articlesDatabase, apiService, access_token),
//            pagingSourceFactory = {
//                articlesDatabase.articlesDao().findAll()
//            }
//        ).liveData
//    }
}