package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PlantDiseaseResultResponse(

	@field:SerializedName("result")
	val result: PlantDiseaseResult,

	@field:SerializedName("meta")
	val meta: PlantDiseaseMeta
) : Parcelable

@Parcelize
data class PlantDiseaseResult(

	@field:SerializedName("detection")
	val detection: Detection,

//	@field:SerializedName("disease")
//	val disease: Any? = null
) : Parcelable

@Parcelize
data class PlantDiseaseMeta(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable

@Parcelize
data class PlantDiseaseDetection(

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

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,
) : Parcelable
