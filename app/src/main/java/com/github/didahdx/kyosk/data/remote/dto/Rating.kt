package com.github.didahdx.kyosk.data.remote.dto

import com.squareup.moshi.Json

data class Rating(
    @Json(name = "quantity")
    val quantity: Int,
    @Json(name = "rate")
    val rate: Double
)