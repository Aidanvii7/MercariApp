package com.mercariapp.feature.browseproducts.presentation

import com.aidanvii.toolbox.adapterviews.databinding.BindableAdapterItem
import com.mercariapp.common.utils.unsafeLazy
import com.mercariapp.core.domain.Product
import com.mercariapp.feature.browseproducts.R
import com.mercariapp.feature.common.presentation.ProductViewModel
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf

internal sealed class ProductAdapterItem : BindableAdapterItem {

    private data class Populated(private val product: Product) : ProductAdapterItem(), KoinComponent {
        override val layoutId get() = R.layout.product
        override val lazyBindableItem = unsafeLazy { get<ProductViewModel> { parametersOf(product) } }
    }

    private object Placeholder : ProductAdapterItem() {
        override val layoutId get() = R.layout.product_placeholder
        override val lazyBindableItem = unsafeLazy { null }
        override val isEmpty get() = true
    }

    companion object {
        fun buildWith(product: Product?): ProductAdapterItem =
            product?.let { Populated(product) } ?: Placeholder
    }
}