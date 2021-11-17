package com.github.didahdx.kyosk.ui.catergory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.didahdx.kyosk.App
import com.github.didahdx.kyosk.R
import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.local.entities.CategoryEntity
import com.github.didahdx.kyosk.databinding.CategoryFragmentBinding
import com.github.didahdx.kyosk.ui.BaseFragment
import com.github.didahdx.kyosk.ui.extensions.hide
import com.github.didahdx.kyosk.ui.extensions.navigateSafe
import com.github.didahdx.kyosk.ui.extensions.snackBar
import com.github.didahdx.kyosk.ui.home.HomeFragment
import com.github.didahdx.kyosk.ui.home.RecyclerViewAdapter
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems


class CategoryFragment : BaseFragment() {
    companion object {
        const val categoryTitle = "CategoryTitle"
    }


    private val categoryViewModel: CategoryViewModel by viewModels()
    private var _binding: CategoryFragmentBinding? = null
    private val binding get() = _binding!!

    var categoryTitle: CategoryEntity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragmentComponent = (requireNotNull(this.activity).application as App)
            .appComponent.getFragmentComponentFactory().create()
        fragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CategoryFragmentBinding.inflate(inflater, container, false)

        val recyclerViewAdapter = RecyclerViewAdapter()
        val gridLayoutManager = GridLayoutManager(binding.root.context, 2)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvCategories.apply {
            layoutManager = gridLayoutManager
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.itemClickListener = { view, item, position ->
            when (item) {
                is RecyclerViewItems.CategoriesChipList -> {
                    //not supported
                }
                is RecyclerViewItems.CategoryChip -> {
                    //not supported
                }
                is RecyclerViewItems.CategoryTitle -> {
                    //not supported
                }
                is RecyclerViewItems.ProductItem -> {
                    val bundle = bundleOf(HomeFragment.productId to item.id)
                    this.findNavController()
                        .navigateSafe(R.id.action_categoryFragment_to_productDetailFragment, bundle)
                }
                is RecyclerViewItems.ProductItemList -> {
                    //not supported
                }
            }
        }
        categoryTitle = arguments?.getParcelable<CategoryEntity>(CategoryFragment.categoryTitle)
        categoryTitle?.let { categoryViewModel.getAllProductByCategory(it.mapToCategoryTitle()) }
        categoryViewModel.item.observe(viewLifecycleOwner, { item ->
            when (item) {
                is Resources.Error -> {
                    item.message?.let { binding.root.snackBar(it) }
                }
                is Resources.Loading -> {
                    //loading
                }
                is Resources.Success -> {
                    binding.progressBar.hide()
                    recyclerViewAdapter.submitList(item.data)
                }
            }

        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}