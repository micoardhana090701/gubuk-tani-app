package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PlantDiseaseResponse(

	@field:SerializedName("result")
	val result: ResultPlantDisease?= null,

	@field:SerializedName("meta")
	val meta: MetaPlantDisease?
) : Parcelable

@Parcelize
data class ResultPlantDisease(

	@field:SerializedName("detection")
	val detection: Detection?,

	@field:SerializedName("disease")
	val disease: Disease?,
) : Parcelable

@Parcelize
data class Detection(

	@field:SerializedName("image")
	val image: String?,

	@field:SerializedName("result")
	val result: String?,

	@field:SerializedName("confidence")
	val confidence: String?,

) : Parcelable

@Parcelize
data class Disease(
	@field:SerializedName("name")
	val name: String?,
	@field:SerializedName("description")
	val description : String?
): Parcelable

@Parcelize
data class MetaPlantDisease(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
) : Parcelable
