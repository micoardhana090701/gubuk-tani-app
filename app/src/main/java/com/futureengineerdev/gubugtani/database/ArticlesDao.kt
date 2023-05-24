package com.futureengineerdev.gubugtani.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: Articles)
    @Query("DELETE FROM articles")
    suspend fun deleteAll(): Int
    @Query("SELECT * FROM articles")
    fun findAll(): PagingSource<Int, Articles>
}