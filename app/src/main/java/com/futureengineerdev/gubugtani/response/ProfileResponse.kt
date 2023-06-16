package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.futureengineerdev.gubugtani.response.Meta
import com.futureengineerdev.gubugtani.response.Result
import com.google.gson.annotations.SerializedName

@Parcelize
data class ProfileResponse(

    @field:SerializedName("result")
	val result: Result,

    @field:SerializedName("meta")
	val meta: Meta
) : Parcelable

@Parcelize
data class Meta(
	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class User(

	@field:SerializedName("profile_photo_url")
	val profilePhotoUrl: String? = null,

	@field:SerializedName("role")
	val role: String?,

	@field:SerializedName("city")
	val city: String?,

	@field:SerializedName("disabled_at")
	val disabledAt: String?,

	@field:SerializedName("type")
	val type: String?,

	@field:SerializedName("created_at")
	val createdAt: String?,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: String?,

	@field:SerializedName("current_team_id")
	val currentTeamId: Int?,

	@field:SerializedName("avatar")
	val avatar: String,

	@field:SerializedName("updated_at")
	val updatedAt: String?,

	@field:SerializedName("name")
	val name: String?,

	@field:SerializedName("id")
	val id: Int?,

	@field:SerializedName("profile_photo_path")
	val profilePhotoPath: String?,

	@field:SerializedName("two_factor_confirmed_at")
	val twoFactorConfirmedAt: String?,

	@field:SerializedName("email")
	val email: String?,

	@field:SerializedName("username")
	val username: String?
) : Parcelable

@Parcelize
data class Result(
	@field:SerializedName("user")
	val user: User
) : Parcelable
