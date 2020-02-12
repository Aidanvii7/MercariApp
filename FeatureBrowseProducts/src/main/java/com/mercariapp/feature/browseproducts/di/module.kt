package com.mercariapp.feature.browseproducts.di

import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.feature.browseproducts.domain.GetProductCategories
import com.mercariapp.feature.browseproducts.domain.GetProductsInCategory
import com.mercariapp.feature.browseproducts.presentation.ProductCategoriesViewModel
import com.mercariapp.feature.browseproducts.presentation.ProductCategoryViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureBrowseProductsModule = module {

    factory { GetProductCategories(repository = get()) }
    factory { GetProductsInCategory(repository = get()) }

    viewModel {
        ProductCategoriesViewModel(
            getProductCategories = get()
        )
    }

    factory { (productCategory: ProductCategory) ->
        ProductCategoryViewModel(
            productCategory = productCategory,
            getProductsInCategory = get()
        )
    }
}