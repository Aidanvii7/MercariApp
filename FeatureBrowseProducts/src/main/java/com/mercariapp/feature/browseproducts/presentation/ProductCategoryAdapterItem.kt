package com.mercariapp.feature.browseproducts.presentation

import com.aidanvii.toolbox.adapterviews.databinding.BindableAdapterItem
import com.mercariapp.common.utils.unsafeLazy
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.feature.browseproducts.R
import com.mercariapp.feature.browseproducts.domain.GetProductsInCategory
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf

internal data class ProductCategoryAdapterItem(
    private val productCategory: ProductCategory
) : BindableAdapterItem, KoinComponent {
    override val layoutId get() = R.layout.product_category
    override val lazyBindableItem = unsafeLazy { get<ProductCategoryViewModel> { parametersOf(productCategory) } }
    override val itemTitle: CharSequence get() = productCategory.name
}