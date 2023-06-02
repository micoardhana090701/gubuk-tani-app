package com.futureengineerdev.gubugtani.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.futureengineerdev.gubugtani.response.User

@Dao
interface ArticlesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: Articles)

    @Query("DELETE FROM articles")
    suspend fun deleteAll(): Int
    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun findAll(): PagingSource<Int, ArticlesWithImages>

    @Query("SELECT * FROM articles WHERE title LIKE :title")
    fun findByTitle(title: String): LiveData<ArticlesWithImages>
}
@Dao
interface ArticleImagesDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: ArticleImages)

    @Query("DELETE FROM articles")
    suspend fun deleteAll(): Int

    @Transaction
    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun findAll(): LiveData<List<ArticlesWithImages>>

}