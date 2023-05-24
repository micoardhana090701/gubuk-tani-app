package com.futureengineerdev.gubugtani.database

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.futureengineerdev.gubugtani.request.ArticleImages
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "articles")
data class Articles (
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("type")
    val type: String?,
    @field:SerializedName("title")
    val title: String?,
    @field:SerializedName("content")
    val content: String?,
    @field:SerializedName("user_id")
    val user_id: Int,
    val article_images: List<ArticleImages>,
//    @field:SerializedName("comment")
//    val comment: List<String>,
) : Parcelable