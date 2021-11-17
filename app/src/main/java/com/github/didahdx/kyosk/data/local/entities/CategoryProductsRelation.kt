package com.github.didahdx.kyosk.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Created by Daniel Didah on 10/22/21.
 */
data class CategoryProductsRelation(
    @Embedded
    val categoryEntity: CategoryEntity,
    @Relation(parentColumn = "code", entityColumn = "category")
    val productEntity: List<ProductEntity>
)
