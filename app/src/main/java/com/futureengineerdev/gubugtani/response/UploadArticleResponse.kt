package com.futureengineerdev.gubugtani.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.futureengineerdev.gubugtani.database.ArticleImages
import com.futureengineerdev.gubugtani.database.ArticleImagesResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadArticleResponse(

	@field:SerializedName("result")
	val result: ResultUpload,

	@field:SerializedName("meta")
	val meta: MetaUpload
):Parcelable

@Parcelize
data class ResultUpload(

	@field:SerializedName("article")
	val article: ArticleUpload
):Parcelable


@Parcelize
data class MetaUpload(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
):Parcelable

@Parcelize
data class ArticleUpload(

	@field:SerializedName("article_images")
	val articleImages: List<ArticleImagesUpload>,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@PrimaryKey(autoGenerate = true)
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String,
): Parcelable

@Parcelize
data class ArticleImagesUpload(
	@PrimaryKey(autoGenerate = true)
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

