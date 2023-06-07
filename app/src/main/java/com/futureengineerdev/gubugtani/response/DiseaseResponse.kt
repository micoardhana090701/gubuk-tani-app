package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DiseaseResponse(

	@field:SerializedName("result")
	val result: ResultDisease,

	@field:SerializedName("meta")
	val meta: MetaDisease
) : Parcelable

@Parcelize
data class ResultDisease(

	@field:SerializedName("per_page")
	val perPage: Int,

	@field:SerializedName("data")
	val data: List<DataItem?>,

	@field:SerializedName("last_page")
	val lastPage: Int,

	@field:SerializedName("first_page_url")
	val firstPageUrl: String,

	@field:SerializedName("path")
	val path: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("last_page_url")
	val lastPageUrl: String,

	@field:SerializedName("from")
	val from: Int,

	@field:SerializedName("links")
	val links: List<LinksItem?>,

	@field:SerializedName("to")
	val to: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("article_id")
	val articleId: Int,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("article")
	val article: Article,

	@field:SerializedName("tags")
	val tags: List<TagsItem?>? = null
) : Parcelable

@Parcelize
data class MetaDisease(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class ArticleImagesItem(

	@field:SerializedName("article_id")
	val articleId: Int,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

) : Parcelable

@Parcelize
data class Article(

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("article_images")
	val articleImages: List<ArticleImagesItem?>,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("content")
	val content: String,
//
//	@field:SerializedName("tags")
//	val tags: List<TagsItem?>? = null
) : Parcelable

@Parcelize
data class LinksItem(

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("label")
	val label: String? = null,

) : Parcelable

@Parcelize
data class TagsItem(

	@field:SerializedName("article_id")
	val articleId: Int? = null,

	@field:SerializedName("tag_id")
	val tagId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("tag")
	val tag: Tag? = null,

	@field:SerializedName("disease_id")
	val diseaseId: Int? = null
) : Parcelable

@Parcelize
data class Tag(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("tag")
	val tag: String
) : Parcelable
