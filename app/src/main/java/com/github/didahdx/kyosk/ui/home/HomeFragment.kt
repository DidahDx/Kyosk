package com.github.didahdx.kyosk.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.didahdx.kyosk.App
import com.github.didahdx.kyosk.R
import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.mapper.mapToCategoryEntity
import com.github.didahdx.kyosk.databinding.HomeFragmentBinding
import com.github.didahdx.kyosk.ui.BaseFragment
import com.github.didahdx.kyosk.ui.catergory.CategoryFragment
import com.github.didahdx.kyosk.ui.extensions.navigateSafe
import com.github.didahdx.kyosk.ui.extensions.snackBar
import timber.log.Timber

class HomeFragment : BaseFragment() {

    // dagger ViewModel
    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!


    companion object{
        const val productId="productId"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireNotNull(this.activity).application as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        val recyclerViewAdapter = RecyclerViewAdapter()
        binding.mainRecyclerview.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = recyclerViewAdapter
        }


        recyclerViewAdapter.itemClickListener = { view, item, position ->
            when (item) {
                is RecyclerViewItems.CategoriesChipList -> {
                    Timber.e(" 1 All $view $item")
                }
                is RecyclerViewItems.CategoryTitle -> {
                    Timber.e("2 Cat Title $view  $item")
                    val bundle= bundleOf(CategoryFragment.categoryTitle to item.mapToCategoryEntity())
                    this.findNavController()
                        .navigateSafe(R.id.action_homeFragment_to_categoryFragment,bundle)
                }
                is RecyclerViewItems.ProductItemList -> {
                    Timber.e(" 3 product List $view  $item")
                }
                is RecyclerViewItems.ProductItem -> {
                    Timber.e("4 product $view  $item")
                    val bundle= bundleOf(productId to item.id)
                    this.findNavController()
                        .navigateSafe(R.id.action_homeFragment_to_productDetailFragment,bundle)
                }
                is RecyclerViewItems.CategoryChip -> {
                    Timber.e("5 Cat Chip $view  $item")
                    homeViewModel.setFilter(item.code)
                }
            }
        }

        homeViewModel.allItems.observe(viewLifecycleOwner, { list ->
                when (list) {
                    is Resources.Loading -> {
                        Timber.e("Loading")
                    }
                    is Resources.Success -> {
                        recyclerViewAdapter.submitList(list.data)
//                        recyclerViewAdapter
                        Timber.d("${list.data?.toString()}")
                    }
                    is Resources.Error -> {
                        list.message?.let { binding.mainRecyclerview.snackBar(it) }
                    }
                }
            })

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        homeViewModel.setFilter("All")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}