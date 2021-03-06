package com.github.didahdx.kyosk.ui.home

import android.os.Parcel
import android.os.Parcelable
import com.github.didahdx.kyosk.data.local.entities.CategoryEntity

/**
 * Created by Daniel Didah on 10/22/21.
 */
sealed class RecyclerViewItems {

    data class ProductItem(
        var category: String,
        var description: String,
        var id: Int,
        var image: String,
        var price: Int,
        var title: String
    ) : RecyclerViewItems()

    data class CategoryTitle(
        var code: String,
        var description: String
    ) : RecyclerViewItems(){
        fun mapToCategoryEntity(): CategoryEntity {
            return CategoryEntity(code, description)
        }
    }

    data class CategoryChip(
        var isSelected: Boolean,
        var code: String,
        var description: String
    ) : RecyclerViewItems()

    data class ProductItemList(
        var productList: List<ProductItem>
    ) : RecyclerViewItems()

    data class CategoriesChipList(
        var categoryTitleList: List<CategoryChip>
    ) : RecyclerViewItems()

}