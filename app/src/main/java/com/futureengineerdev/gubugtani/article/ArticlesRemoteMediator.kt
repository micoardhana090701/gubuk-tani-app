package com.futureengineerdev.gubugtani.article

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.futureengineerdev.gubugtani.api.ApiService
import com.futureengineerdev.gubugtani.database.ArticleImages
import com.futureengineerdev.gubugtani.database.Articles
import com.futureengineerdev.gubugtani.database.ArticlesDatabase
import com.futureengineerdev.gubugtani.database.RemoteKeys
import com.futureengineerdev.gubugtani.etc.UserPreferences
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
class ArticlesRemoteMediator(private val articlesDatabase: ArticlesDatabase, private val apiService: ApiService, private val access_token: UserPreferences): RemoteMediator<Int, Articles>() {

    private companion object{
        const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Articles>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1)?: STARTING_PAGE_INDEX
            }

            LoadType.PREPEND ->{
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND ->{
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val access_token = "Bearer ${access_token.getUserKey().first()}"
            val responseData = apiService.getArticles(access_token, page, state.config.pageSize)
            val endOfPaginationReached = responseData.result.next_page_url == null

            articlesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH){
                    articlesDatabase.remoteKeysDao().deleteRemoteKeys()
                    articlesDatabase.articlesDao().deleteAll()
                    articlesDatabase.articleImagesDao().deleteAll()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.result.data.map {
                    RemoteKeys(
                        it.id,
                        prevKey,
                        nextKey
                    )
                }
                articlesDatabase.remoteKeysDao().insertAll(keys)
                responseData.result.data.map{ item ->
                    val article = Articles(
                        item.id,
                        item.type,
                        item.title,
                        item.content,
                        item.user_id,
                        item.article_images
                    )
                    articlesDatabase.articlesDao().insertAll(article)

                    item.article_images.imageList.map { image ->
                        val articleImages = ArticleImages(
                            image.id,
                            image.image,
                            image.article_id,
                            image.deleted_at,
                            image.created_at,
                            image.updated_at,
                        )
                        articlesDatabase.articleImagesDao().insertAll(articleImages)
                    }
                }

            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception){
            Log.e("ArticlesRemoteMediator", "load: ${e.message}", e)
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Articles>): RemoteKeys? {
        return state.pages.lastOrNull(){it.data.isNotEmpty()}?.data?.lastOrNull()?.let { article ->
            articlesDatabase.remoteKeysDao().getRemoteKeysId(article.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Articles>): RemoteKeys? {
        return state.pages.firstOrNull{it.data.isNotEmpty()}?.data?.firstOrNull()?.let { article ->
            articlesDatabase.remoteKeysDao().getRemoteKeysId(article.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Articles>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { articleId ->
                articlesDatabase.remoteKeysDao().getRemoteKeysId(articleId)
            }
        }
    }
}