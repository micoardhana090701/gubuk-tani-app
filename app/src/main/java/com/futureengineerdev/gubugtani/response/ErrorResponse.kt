package com.futureengineerdev.gubugtani.response

data class ErrorResponse(
    val meta: MetaData,
    val errors: ErrorData
)

data class ErrorData(
    val username: List<String>?,
    val email: List<String>?
)