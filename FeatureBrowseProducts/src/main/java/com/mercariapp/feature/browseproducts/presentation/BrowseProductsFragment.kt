package com.mercariapp.feature.browseproducts.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mercariapp.feature.browseproducts.databinding.FragmentBrowseProductsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class BrowseProductsFragment : Fragment() {

    private val productCategoriesViewModel by viewModel<ProductCategoriesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBrowseProductsBinding.inflate(inflater, container, false).run {
        lifecycleOwner = this@BrowseProductsFragment
        viewModel = productCategoriesViewModel
        root
    }
}