package com.github.didahdx.kyosk.data.mapper

import com.github.didahdx.kyosk.data.local.entities.CategoryEntity
import com.github.didahdx.kyosk.data.local.entities.CategoryProductsRelation
import com.github.didahdx.kyosk.data.local.entities.ProductEntity
import com.github.didahdx.kyosk.data.remote.dto.Category
import com.github.didahdx.kyosk.data.remote.dto.ProductItemDto
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems

/**
 * Created by Daniel Didah on 10/22/21.
 */


fun ProductItemDto.mapToProductEntity(): ProductEntity {
    return ProductEntity(category, description, id, image, price, title)
}

fun Category.mapToCategoryEntity(): CategoryEntity {
    return CategoryEntity(code, description)
}

fun ProductEntity.mapToProductItem(): RecyclerViewItems.ProductItem {
    return RecyclerViewItems.ProductItem(category, description, id, image, price, title)
}

fun CategoryEntity.mapToCategoryTitle(): RecyclerViewItems.CategoryTitle {
    return RecyclerViewItems.CategoryTitle( code, description)
}
fun CategoryEntity.mapToCategoryChip(): RecyclerViewItems.CategoryChip {
    return RecyclerViewItems.CategoryChip( code, description)
}

fun CategoryProductsRelation.mapToProductItemList(): RecyclerViewItems.ProductItemList {
    return RecyclerViewItems.ProductItemList(this.productEntity.map { productEntity -> productEntity.mapToProductItem() })
}

fun List<CategoryEntity>.mapToCategoryChipList(): RecyclerViewItems.CategoriesChipList {
    val allOption = CategoryEntity("All", "All")
    val list = this.toMutableList()
    list.add(0,allOption)
    return RecyclerViewItems.CategoriesChipList(list.map { categoryEntity -> categoryEntity.mapToCategoryChip() })
}

fun RecyclerViewItems.CategoryTitle.mapToCategoryEntity():CategoryEntity{
    return CategoryEntity(code, description)
}