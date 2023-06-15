package com.futureengineerdev.gubugtani.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PaymentMethodResponse(

	@field:SerializedName("result")
	val result: ResultMethod,

	@field:SerializedName("meta")
	val meta: MetaMethod
) : Parcelable

@Parcelize
data class ResultMethod(

	@field:SerializedName("payment_methods")
	val paymentMethods: List<PaymentMethodsItem>
) : Parcelable

@Parcelize
data class PaymentMethodsItem(

	@field:SerializedName("number")
	val number: String? = null,

	@field:SerializedName("method")
	val method: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

) : Parcelable

@Parcelize
data class MetaMethod(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
