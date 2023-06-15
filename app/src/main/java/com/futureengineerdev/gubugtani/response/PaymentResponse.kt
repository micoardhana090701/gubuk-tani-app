package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PaymentResponse(

	@field:SerializedName("result")
	val result: ResultPayment? = null,

	@field:SerializedName("meta")
	val meta: MetaPayment? = null
) : Parcelable

@Parcelize
data class Payment(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("payment_method_id")
	val paymentMethodId: String? = null,

	@field:SerializedName("notes")
	val notes: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class ResultPayment(

	@field:SerializedName("payment")
	val payment: Payment? = null
) : Parcelable

@Parcelize
data class MetaPayment(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
