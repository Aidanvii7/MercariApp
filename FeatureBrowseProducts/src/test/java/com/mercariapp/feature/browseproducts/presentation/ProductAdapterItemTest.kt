package com.mercariapp.feature.browseproducts.presentation

import com.mercariapp.core.domain.Product
import com.mercariapp.feature.browseproducts.R
import com.mercariapp.feature.common.di.featureCommonModule
import com.mercariapp.testutils.KoinExtension
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be null`
import org.amshove.kluent.`should be true`
import org.amshove.kluent.`should not be null`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

internal class ProductAdapterItemTest {

    @JvmField
    @RegisterExtension
    val koinExtension = KoinExtension(featureCommonModule)

    @Nested
    @DisplayName("With a ProductViewModel that is on sale")
    inner class OnSale : ProductAdapterItemTest(
        Product(
            id = "onSaleProduct id",
            name = "onSaleProduct name",
            status = Product.Status.ON_SALE,
            likeCount = 100,
            commentCount = 200,
            priceInYen = 300,
            photoUrl = "https://dummyimage.com/400x400/000/fff?text=men1"
        )
    ) {

        @Test
        @DisplayName("layoutId is R.layout.product")
        fun layoutIdCorrect() {
            tested.layoutId `should be equal to` R.layout.product
        }

        @Test
        @DisplayName("isEmpty is false")
        fun isNotEmpty() {
            tested.isEmpty.`should be false`()
        }

        @Test
        @DisplayName("lazyBindableItem's value is not null")
        fun bindableItemNotNull() {
            tested.lazyBindableItem.value.`should not be null`()
        }
    }

    @Nested
    @DisplayName("With a ProductViewModel that is sold out")
    inner class SoldOut : ProductAdapterItemTest(product = null) {

        @Test
        @DisplayName("layoutId is R.layout.product_placeholder")
        fun layoutIdCorrect() {
            tested.layoutId `should be equal to` R.layout.product_placeholder
        }

        @Test
        @DisplayName("isEmpty is true")
        fun isEmpty() {
            tested.isEmpty.`should be true`()
        }

        @Test
        @DisplayName("lazyBindableItem's value is null")
        fun bindableItemNull() {
            tested.lazyBindableItem.value.`should be null`()
        }
    }

    abstract inner class ProductAdapterItemTest(product: Product?) {
        val tested = ProductAdapterItem.buildWith(product)
    }
}