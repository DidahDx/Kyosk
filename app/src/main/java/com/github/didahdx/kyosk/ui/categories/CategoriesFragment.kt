package com.github.didahdx.kyosk.ui.categories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.didahdx.kyosk.App
import com.github.didahdx.kyosk.R
import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.mapper.mapToCategoryEntity
import com.github.didahdx.kyosk.databinding.CategoriesFragmentBinding
import com.github.didahdx.kyosk.ui.BaseFragment
import com.github.didahdx.kyosk.ui.catergory.CategoryFragment.Companion.categoryTitle
import com.github.didahdx.kyosk.ui.extensions.navigateSafe
import com.github.didahdx.kyosk.ui.extensions.snackBar
import com.github.didahdx.kyosk.ui.home.RecyclerViewAdapter
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems
import timber.log.Timber
import javax.inject.Inject

class CategoriesFragment : BaseFragment() {
//    @Inject
//    lateinit var viewModel: ViewModelProvider.Factory
    private var _binding: CategoriesFragmentBinding? = null
    private val binding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by viewModels()
//    private lateinit var categoriesViewModel: CategoriesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireNotNull(this.activity).application as App).appComponent.inject(this)
//        categoriesViewModel = ViewModelProvider(this,viewModel).get(CategoriesViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= CategoriesFragmentBinding.inflate(inflater,container,false)
        val recyclerViewAdapter = RecyclerViewAdapter()
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = recyclerViewAdapter
        }

        recyclerViewAdapter.itemClickListener = { view,item,position ->
            when(item){
                is RecyclerViewItems.CategoriesChipList -> {}
                is RecyclerViewItems.CategoryChip ->{}
                is RecyclerViewItems.CategoryTitle -> {
                    val bundle= bundleOf(categoryTitle to item.mapToCategoryEntity())
                    this.findNavController()
                        .navigateSafe(R.id.action_categoriesFragment_to_categoryFragment,bundle)
                }
                is RecyclerViewItems.ProductItem -> {

                }
                is RecyclerViewItems.ProductItemList -> {

                }
            }
        }

        categoriesViewModel.categories.observe(viewLifecycleOwner,{
            when(it){
                is Resources.Error -> {
                    it.message?.let { message -> binding.rvCategories.snackBar(message) }
                }
                is Resources.Loading -> {
                    //loading
                }
                is Resources.Success -> {
                    Timber.e("${it.data}")
                    recyclerViewAdapter.submitList(it.data)
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