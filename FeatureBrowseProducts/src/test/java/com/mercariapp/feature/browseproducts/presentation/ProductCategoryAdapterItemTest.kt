package com.mercariapp.feature.browseproducts.presentation

import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.domain.ProductRepository
import com.mercariapp.feature.browseproducts.R
import com.mercariapp.feature.browseproducts.di.featureBrowseProductsModule
import com.mercariapp.testutils.KoinExtension
import com.nhaarman.mockitokotlin2.mock
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should not be null`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.dsl.module

internal class ProductCategoryAdapterItemTest {

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(featureBrowseProductsModule, module { single { mock<ProductRepository>() } })

    val tested = ProductCategoryAdapterItem(
        productCategory = ProductCategory(
            name = "All",
            dataEndpoint = "https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/all.json"
        )
    )

    @Test
    @DisplayName("layoutId is R.layout.product_category")
    fun layoutIdCorrect() {
        tested.layoutId `should be equal to` R.layout.product_category
    }

    @Test
    @DisplayName("isEmpty is false")
    fun isEmpty() {
        tested.isEmpty.`should be false`()
    }

    @Test
    @DisplayName("lazyBindableItem's value is not null")
    fun bindableItemNull() {
        tested.lazyBindableItem.value.`should not be null`()
    }
}