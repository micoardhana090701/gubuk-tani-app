package com.futureengineerdev.gubugtani.response

import com.google.gson.annotations.SerializedName

data class ResultLogin(
    @field:SerializedName("access_token")
    val access_token: String,
    @field:SerializedName("token_type")
    val token_type: String,
)