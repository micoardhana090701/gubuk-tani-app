package com.futureengineerdev.gubugtani.request

import com.futureengineerdev.gubugtani.database.ArticlesResponseData
import com.google.gson.annotations.SerializedName

data class ShowArticles(
    @field:SerializedName("data")
    val data: List<ArticlesResponseData>,
)