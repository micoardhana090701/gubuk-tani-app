package com.futureengineerdev.gubugtani.etc

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.article.ArticleRepository
import com.futureengineerdev.gubugtani.database.ArticlesDatabase

object Injection {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    fun provideRepository(context: Context): ArticleRepository {
        val database = ArticlesDatabase.getDatabase(context)
        val apiService = ApiConfig.apiInstance
        val pref = UserPreferences.getInstance(context.dataStore)
        return ArticleRepository(database, apiService, pref)
    }
}