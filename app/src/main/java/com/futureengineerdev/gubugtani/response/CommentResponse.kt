package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class CommentResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
) : Parcelable

@Parcelize
data class ResultComment(

	@field:SerializedName("per_page")
	val perPage: Int?,

	@field:SerializedName("data")
	val data: List<String>?,

	@field:SerializedName("last_page")
	val lastPage: Int?,

	@field:SerializedName("first_page_url")
	val firstPageUrl: String,

	@field:SerializedName("path")
	val path: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("last_page_url")
	val lastPageUrl: String,

	@field:SerializedName("from")
	val from: String,

	@field:SerializedName("links")
	val links: List<LinksItemComment?>,

	@field:SerializedName("to")
	val to: String,

	@field:SerializedName("current_page")
	val currentPage: Int
) : Parcelable

@Parcelize
data class MetaComment(

	@field:SerializedName("code")
	val code: Int?,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class LinksItemComment(

	@field:SerializedName("active")
	val active: Boolean?,

	@field:SerializedName("label")
	val label: String?,

	@field:SerializedName("url")
	val url: String?
) : Parcelable
