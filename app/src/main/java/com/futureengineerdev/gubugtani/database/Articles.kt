package com.futureengineerdev.gubugtani.database

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "articles")
data class Articles(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("type")
    val type: String,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("content")
    val content: String,
    @field:SerializedName("user_id")
    val user_id: Int,
    @field:SerializedName("created_at")
    val created_at: String = System.currentTimeMillis().toString(),
) : Parcelable

@Parcelize
data class ArticlesWithImages(
    @Embedded
    val articles: Articles,

    @Relation(
        parentColumn = "id",
        entityColumn = "article_id"
    )
    val article_images: List<ArticleImages>
): Parcelable



@Parcelize
@Entity(tableName = "article_images")
data class ArticleImages(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("image")
    val image: String?,
    @field:SerializedName("article_id")
    val article_id: Int,
    @field:SerializedName("deleted_at")
    val deleted_at: String?,
    @field:SerializedName("created_at")
    val created_at: String?,
    @field:SerializedName("updated_at")
    val updated_at: String?
): Parcelable