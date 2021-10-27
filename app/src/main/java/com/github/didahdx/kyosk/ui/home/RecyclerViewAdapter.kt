package com.github.didahdx.kyosk.ui.home


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.github.didahdx.kyosk.R
import com.github.didahdx.kyosk.databinding.*
import com.github.didahdx.kyosk.ui.staterestorationadapter.NestedRecyclerViewStateRecoverAdapter

/**
 * Created by Daniel Didah on 10/24/21.
 */
class RecyclerViewAdapter :
    NestedRecyclerViewStateRecoverAdapter<RecyclerViewItems, RecyclerViewHolder>(RecyclerViewDiffUtil()) {
    var itemClickListener: ((view: View, item: RecyclerViewItems, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return when (viewType) {
            R.layout.item_horizontal_view -> {
                RecyclerViewHolder.ProductsViewHolderList(
                    ItemHorizontalViewBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent, false
                    )
                )
            }
            R.layout.item_horizontal_category -> {
                RecyclerViewHolder.CategoriesChipViewHolderList(
                    ItemHorizontalCategoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.item_categories_chips ->{
                RecyclerViewHolder.CategoryChipViewHolder(ItemCategoriesChipsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ))
            }
            R.layout.item_product ->{
                RecyclerViewHolder.ProductItemViewHolder(
                    ItemProductBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.item_title_header -> {
                RecyclerViewHolder.CategoryTitleViewHolder(
                    ItemTitleHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false))
            }
            else -> throw IllegalArgumentException("Invalid Type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener
        when (holder) {
            is RecyclerViewHolder.CategoriesChipViewHolderList -> {
                holder.bind(getItem(position) as RecyclerViewItems.CategoriesChipList)
//                holder.setChipCheckChip(position)
            }
            is RecyclerViewHolder.ProductsViewHolderList -> holder.bind(getItem(position) as RecyclerViewItems.ProductItemList)
            is RecyclerViewHolder.ProductItemViewHolder -> holder.bind(getItem(position) as RecyclerViewItems.ProductItem)
            is RecyclerViewHolder.CategoryChipViewHolder -> holder.bind(getItem(position) as RecyclerViewItems.CategoryChip)
            is RecyclerViewHolder.CategoryTitleViewHolder -> holder.bind(getItem(position) as RecyclerViewItems.CategoryTitle)
        }
        super.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecyclerViewItems.ProductItemList -> R.layout.item_horizontal_view
            is RecyclerViewItems.CategoriesChipList -> R.layout.item_horizontal_category
            is RecyclerViewItems.CategoryTitle -> R.layout.item_title_header
            is RecyclerViewItems.ProductItem -> R.layout.item_product
            is RecyclerViewItems.CategoryChip ->R.layout.item_categories_chips
        }
    }
}


class RecyclerViewDiffUtil : DiffUtil.ItemCallback<RecyclerViewItems>() {
    override fun areItemsTheSame(oldItem: RecyclerViewItems, newItem: RecyclerViewItems): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: RecyclerViewItems,
        newItem: RecyclerViewItems
    ): Boolean {
        return oldItem == newItem
    }

}