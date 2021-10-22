package com.github.didahdx.kyosk.data.remote.dto

import com.squareup.moshi.Json

data class Category(
    @Json(name = "code")
    val code: String,
    @Json(name = "description")
    val description: String
)