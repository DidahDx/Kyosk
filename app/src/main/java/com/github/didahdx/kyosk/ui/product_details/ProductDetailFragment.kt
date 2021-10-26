package com.github.didahdx.kyosk.ui.product_details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.didahdx.kyosk.App
import com.github.didahdx.kyosk.R
import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.databinding.ProductDetailFragmentBinding
import com.github.didahdx.kyosk.ui.BaseFragment
import com.github.didahdx.kyosk.ui.extensions.snackBar
import com.github.didahdx.kyosk.ui.home.HomeFragment
import com.github.didahdx.kyosk.ui.home.HomeViewModel
import javax.inject.Inject

class ProductDetailFragment : BaseFragment() {

//    @Inject
//    lateinit var viewModel: ViewModelProvider.Factory
    // dagger ViewModel
    private val productDetailViewModel: ProductDetailViewModel by viewModels()

    private var _binding: ProductDetailFragmentBinding? = null
    private val binding get() = _binding!!

    var productId: Int? =0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireNotNull(this.activity).application as App).appComponent.inject(this)
//        productDetailViewModel = ViewModelProvider(this,viewModel).get(ProductDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDetailFragmentBinding.inflate(inflater,container,false)
        productId = arguments?.getInt(HomeFragment.productId)
        productId?.let { productDetailViewModel.getProduct(it) }
        productDetailViewModel.items.observe(viewLifecycleOwner,{item->
            when(item){
                is Resources.Error -> {
                    item.message?.let { binding.root.snackBar(it) }
                }
                is Resources.Loading -> {
                    //loading
                }
                is Resources.Success -> {
                    binding.description.text= item.data?.description
                    binding.title.text= item.data?.title
                    binding.price.text= getString(R.string.currency_kes, item.data?.price)
                    Glide
                        .with(binding.root.context)
                        .load(item.data?.image)
                        .centerInside()
                        .placeholder(R.drawable.place_holder)
                        .error(R.drawable.not_available)
                        .into(binding.productImage)

                    binding.btnBack.setOnClickListener {
                        this.findNavController().navigateUp()
                    }
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