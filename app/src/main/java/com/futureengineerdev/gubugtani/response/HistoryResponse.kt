package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class HistoryResponse(

	@field:SerializedName("result")
	val result: ResultHistory? = null,

	@field:SerializedName("meta")
	val meta: MetaHistory? = null
) : Parcelable

@Parcelize
data class UserHistory(

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("avatar")
	val avatar: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

) : Parcelable

@Parcelize
data class DataItemHistory(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("plant_id")
	val plantId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("confidence")
	val confidence: String? = null,

	@field:SerializedName("plant")
	val plant: PlantHistory? = null,

	@field:SerializedName("created_at")
	val createdAt: String = System.currentTimeMillis().toString(),

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("user")
	val user: User? = null
) : Parcelable

@Parcelize
data class LinksItemHistory(

	@field:SerializedName("active")
	val active: Boolean? = null,

	@field:SerializedName("label")
	val label: String? = null,

) : Parcelable

@Parcelize
data class ResultHistory(

	@field:SerializedName("data")
	val data: List<DataItemHistory?>? = null,

	@field:SerializedName("links")
	val links: List<LinksItemHistory?>? = null,

) : Parcelable

@Parcelize
data class MetaHistory(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class PlantHistory(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
