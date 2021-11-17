package com.github.didahdx.kyosk.data.mapper

import com.github.didahdx.kyosk.data.local.entities.CategoryEntity
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems

/**
 * Created by Daniel Didah on 10/22/21.
 */


fun List<CategoryEntity>.mapToCategoryChipList(code: String): RecyclerViewItems.CategoriesChipList {
    val allOption = CategoryEntity("All", "All")
    val list = this.toMutableList()
    list.add(0, allOption)
    return RecyclerViewItems.CategoriesChipList(list.map { categoryEntity ->
        categoryEntity.mapToCategoryChip(
            code
        )
    })
}