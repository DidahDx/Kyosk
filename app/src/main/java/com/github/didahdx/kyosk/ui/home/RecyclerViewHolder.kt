package com.github.didahdx.kyosk.ui.home

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.github.didahdx.kyosk.R
import com.github.didahdx.kyosk.databinding.*
import com.github.didahdx.kyosk.ui.staterestorationadapter.NestedRecyclerViewViewHolder
import com.google.android.material.chip.Chip

/**
 * Created by Daniel Didah on 10/24/21.
 */
sealed class RecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: RecyclerViewItems, position: Int) -> Unit)? = null

    class CategoriesChipViewHolderList(private val binding: ItemHorizontalCategoryBinding) :
        RecyclerViewHolder(binding) {
        fun setChipCheckChip(chipId: Int) {
            binding.chipGroup.check(chipId)
        }

        fun bind(categoriesChipList: RecyclerViewItems.CategoriesChipList) {
            binding.chipGroup.removeAllViews()
            for (item in categoriesChipList.categoryTitleList.indices) {
                val chip = Chip(binding.chipGroup.context)
                val categoryChip = categoriesChipList.categoryTitleList[item]
                chip.text = categoryChip.description
                chip.id = item
                if (categoryChip.isSelected) {
                    chip.isChecked = true
                }
                binding.chipGroup.addView(chip)
            }
            binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
                itemClickListener?.invoke(
                    group,
                    categoriesChipList.categoryTitleList[checkedId],
                    adapterPosition
                )
            }
        }
    }

    class CategoryTitleViewHolder(
        private val binding: ItemTitleHeaderBinding
    ) : RecyclerViewHolder(binding) {
        fun bind(categoryTitle: RecyclerViewItems.CategoryTitle) {
            binding.title.text = categoryTitle.description
            binding.viewAll.setOnClickListener {
                itemClickListener?.invoke(it, categoryTitle, adapterPosition)
            }
        }
    }

    class ProductsViewHolderList(private val binding: ItemHorizontalViewBinding) :
        RecyclerViewHolder(binding), NestedRecyclerViewViewHolder {
        lateinit var productItemList: RecyclerViewItems.ProductItemList

        init {
            binding.rvCategory.apply {
                layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = RecyclerViewAdapter()
            }
        }

        fun bind(productItemList: RecyclerViewItems.ProductItemList) {
            this.productItemList = productItemList
            with(binding) {
                (rvCategory.adapter as RecyclerViewAdapter).itemClickListener = itemClickListener
                (rvCategory.adapter as RecyclerViewAdapter).submitList(productItemList.productList)
            }
        }

        override fun getId() = productItemList.productList.first().category


        override fun getLayoutManager() = binding.rvCategory.layoutManager
    }

    class ProductItemViewHolder(private val binding: ItemProductBinding) :
        RecyclerViewHolder(binding) {
        fun bind(productItem: RecyclerViewItems.ProductItem) {
            binding.description.text = productItem.description
            binding.price.text =
                binding.root.context.getString(R.string.currency_kes, productItem.price)
            Glide
                .with(binding.root.context)
                .load(productItem.image)
                .centerCrop()
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.not_available)
                .into(binding.image)

            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, productItem, adapterPosition)
            }
        }
    }

    //used to display individual chips for the categories
    class CategoryChipViewHolder(private val binding: ItemCategoriesChipsBinding) :
        RecyclerViewHolder(binding) {
        fun bind(categoryChip: RecyclerViewItems.CategoryChip) {
            binding.chip.text = categoryChip.description
            binding.chip.setOnClickListener {
                itemClickListener?.invoke(it, categoryChip, adapterPosition)
            }
        }
    }

}