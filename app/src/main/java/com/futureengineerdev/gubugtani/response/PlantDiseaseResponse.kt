package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PlantDiseaseResponse(

	@field:SerializedName("result")
	val result: ResultPlantDisease,

	@field:SerializedName("meta")
	val meta: MetaPlantDisease
) : Parcelable

@Parcelize
data class ResultPlantDisease(

	@field:SerializedName("detection")
	val detection: Detection,

//	@field:SerializedName("disease")
//	val disease: String? = null
) : Parcelable

@Parcelize
data class Detection(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("plant_id")
	val plantId: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("confidence")
	val confidence: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int?,

) : Parcelable

@Parcelize
data class MetaPlantDisease(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable
