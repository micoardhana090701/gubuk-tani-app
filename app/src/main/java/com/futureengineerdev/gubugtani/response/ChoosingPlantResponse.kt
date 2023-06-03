package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ChoosingPlantResponse(

	@field:SerializedName("result")
	val result: ResultPlant,

	@field:SerializedName("meta")
	val meta: MetaPlant
) : Parcelable

@Parcelize
data class LabelsItem(

	@field:SerializedName("plant_id")
	val plantId: Int?,


	@field:SerializedName("name")
	val name: String?,


	@field:SerializedName("id")
	val id: Int?,

) : Parcelable

@Parcelize
data class PlantsItem(

	@field:SerializedName("image")
	val image: String?,

	@field:SerializedName("updated_at")
	val updatedAt: String?,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("created_at")
	val createdAt: String?,

	@field:SerializedName("id")
	val id: Int?,

	@field:SerializedName("status")
	val status: String?,

	@field:SerializedName("labels")
	val labels: List<LabelsItem?>?
) : Parcelable

@Parcelize
data class ResultPlant(

	@field:SerializedName("plants")
	val plants: List<PlantsItem>,
) : Parcelable

@Parcelize
data class MetaPlant(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
