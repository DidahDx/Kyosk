package com.github.didahdx.kyosk.data.remote.dto

import com.squareup.moshi.Json

data class CategoryListDto(
    @Json(name = "categories")
    val categories: List<Category>
)