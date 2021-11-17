package com.github.didahdx.kyosk.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems


/**
 * Created by Daniel Didah on 10/22/21.
 */
@Entity
data class ProductEntity(
    var category: String,
    var description: String,
    @PrimaryKey
    var id: Int,
    var image: String,
    var price: Int,
    var title: String
){


    fun mapToProductItem(): RecyclerViewItems.ProductItem {
        return RecyclerViewItems.ProductItem(category, description, id, image, price, title)
    }
}