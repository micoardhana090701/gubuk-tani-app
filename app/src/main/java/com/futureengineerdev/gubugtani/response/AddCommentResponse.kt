package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AddCommentResponse(

	@field:SerializedName("result")
	val result: ResultAddComment? = null,

	@field:SerializedName("meta")
	val meta: MetaAddComment? = null
) : Parcelable

@Parcelize
data class UserAddComment(
	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,
) : Parcelable

@Parcelize
data class MetaAddComment(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ResultAddComment(

	@field:SerializedName("comment")
	val comment: CommentAddComment? = null
) : Parcelable

@Parcelize
data class CommentAddComment(

	@field:SerializedName("article_id")
	val articleId: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("comment")
	val comment: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("user")
	val user: UserAddComment? = null
) : Parcelable


