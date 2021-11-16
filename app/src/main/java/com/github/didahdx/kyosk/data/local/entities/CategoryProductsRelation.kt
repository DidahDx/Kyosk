package com.github.didahdx.kyosk.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems

/**
 * Created by Daniel Didah on 10/22/21.
 */
data class CategoryProductsRelation(
    @Embedded
    val categoryEntity: CategoryEntity,
    @Relation(parentColumn = "code", entityColumn = "category")
    val productEntity: List<ProductEntity>
){

    fun mapToProductItemList(): RecyclerViewItems.ProductItemList {
        return RecyclerViewItems.ProductItemList(this.productEntity.map { productEntity -> productEntity.mapToProductItem() })
    }
}
