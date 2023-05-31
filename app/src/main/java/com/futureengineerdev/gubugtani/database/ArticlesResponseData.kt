package com.futureengineerdev.gubugtani.database

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class ArticlesResponseData (
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
    @field:SerializedName("article_images")
    val article_images: List<ArticleImagesResponse>,
    @field:SerializedName("created_at")
    val created_at: String = System.currentTimeMillis().toString(),
) : Parcelable


@Parcelize
data class ArticleImagesResponse(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("image")
    val image: String,
    @field:SerializedName("article_id")
    val article_id: Int,
    @field:SerializedName("deleted_at")
    val deleted_at: String,
    @field:SerializedName("created_at")
    val created_at: String,
    @field:SerializedName("updated_at")
    val updated_at: String
): Parcelable