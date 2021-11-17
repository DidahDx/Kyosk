package com.github.didahdx.kyosk.data.remote.dto

import com.github.didahdx.kyosk.data.local.entities.CategoryEntity
import com.squareup.moshi.Json

data class Category(
    @Json(name = "code")
    val code: String,
    @Json(name = "description")
    val description: String
){

    fun mapToCategoryEntity(): CategoryEntity {
        return CategoryEntity(code, description)
    }
}