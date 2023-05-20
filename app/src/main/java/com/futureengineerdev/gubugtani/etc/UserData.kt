package com.futureengineerdev.gubugtani.etc

import com.google.gson.annotations.SerializedName

data class UserData (
    @SerializedName("id")
    val id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("username")
    val username: String?,

    @SerializedName("avatar")
    val avatar: String?,

    @SerializedName("created_at")
    val created_at: String?,

    @SerializedName("updated_at")
    val updated_at: String?


)