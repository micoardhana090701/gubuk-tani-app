package com.futureengineerdev.gubugtani.article

import com.futureengineerdev.gubugtani.database.ArticlesResponseData
import com.futureengineerdev.gubugtani.article.Meta
import com.futureengineerdev.gubugtani.article.Result
import com.google.gson.annotations.SerializedName


data class ArticlesResponse(
	@field:SerializedName("meta")
	val meta: Meta,
	@field:SerializedName("result")
	val result: Result,
)

data class Meta(
	@field:SerializedName("code")
	val code: Int?,
	@field:SerializedName("status")
	val status: String?,
	@field:SerializedName("message")
	val message: String?
)

data class Result(
	@field:SerializedName("current_page")
	val current_page: Int?,
	@field:SerializedName("data")
	val data: List<ArticlesResponseData>,
	val first_page_url: String?,
	val from: Int?,
	val last_page: Int?,
	val last_page_url: String?,
	val links: List<LinkItem>?,
	val next_page_url: String?,
	val path: String?,
	val per_page: Int?,
	val prev_page_url: String?,
	val to: Int?,
	val total: Int?
)


data class Tag(
	val id: Int?,
	val tag_id: Int?,
	val article_id: Int?,
	val tag: TagInfo?
)

data class TagInfo(
	val id: Int?,
	val tag: String?
)

data class LinkItem(
	val url: String?,
	val label: String?,
	val active: Boolean?
)
