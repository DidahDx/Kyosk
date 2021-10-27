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
import com.github.didahdx.kyosk.ui.extensions.hide
import com.github.didahdx.kyosk.ui.extensions.navigateSafe
import com.github.didahdx.kyosk.ui.extensions.snackBar
import timber.log.Timber

class HomeFragment : BaseFragment() {

    // dagger ViewModel
    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val productId = "productId"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val appComp = (requireNotNull(this.activity).application as App).appComponent
        appComp.inject(this)
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


        recyclerViewAdapter.itemClickListener = { _, item, _ ->
            when (item) {
                is RecyclerViewItems.CategoriesChipList -> {
                    //not used
                }
                is RecyclerViewItems.CategoryTitle -> {

                    val bundle =
                        bundleOf(CategoryFragment.categoryTitle to item.mapToCategoryEntity())
                    this.findNavController()
                        .navigateSafe(R.id.action_homeFragment_to_categoryFragment, bundle)
                }
                is RecyclerViewItems.ProductItemList -> {
                    //not used
                }
                is RecyclerViewItems.ProductItem -> {
                    //individual product clicked
                    val bundle = bundleOf(productId to item.id)
                    this.findNavController()
                        .navigateSafe(R.id.action_homeFragment_to_productDetailFragment, bundle)
                }
                is RecyclerViewItems.CategoryChip -> {
                    //category chip clicked
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
                    binding.progressBar.hide()
                    recyclerViewAdapter.submitList(list.data)
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