package com.github.didahdx.kyosk.data.remote.dto

import com.github.didahdx.kyosk.data.local.entities.ProductEntity
import com.squareup.moshi.Json

data class ProductItemDto(
    @Json(name = "category")
    val category: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "image")
    val image: String,
    @Json(name = "price")
    val price: Int,
    @Json(name = "rating")
    val rating: Rating,
    @Json(name = "title")
    val title: String
)