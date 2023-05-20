package com.futureengineerdev.gubugtani.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @field:SerializedName("status")
    val status: String?,

    @field:SerializedName("message")
    val message: String?
)