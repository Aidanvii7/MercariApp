package com.mercariapp.feature.common.di

import com.mercariapp.core.domain.Product
import com.mercariapp.feature.common.presentation.ProductViewModel
import org.koin.dsl.module

val featureCommonModule = module {
    factory { (product: Product) ->
        ProductViewModel(product)
    }
}