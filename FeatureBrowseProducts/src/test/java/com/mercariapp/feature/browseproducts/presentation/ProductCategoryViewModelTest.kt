package com.mercariapp.feature.browseproducts.presentation

import com.mercariapp.core.domain.Product
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.domain.ProductRepository
import com.mercariapp.feature.browseproducts.domain.GetProductsInCategory
import com.mercariapp.testutils.synchronousCoroutineDispatchers
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class ProductCategoryViewModelTest {

    val productCategory = ProductCategory(
        name = "All",
        dataEndpoint = "https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/all.json"
    )

    val product1 = Product(
        id = "product1 id",
        name = "product1 name",
        status = Product.Status.ON_SALE,
        likeCount = 1,
        commentCount = 2,
        priceInYen = 3,
        photoUrl = "https://dummyimage.com/400x400/000/fff?text=men1"
    )
    val product2 = Product(
        id = "product2 id",
        name = "product2 name",
        status = Product.Status.SOLD_OUT,
        likeCount = 1,
        commentCount = 2,
        priceInYen = 3,
        photoUrl = "https://dummyimage.com/400x400/000/fff?text=men2"
    )
    val products = listOf(product1, product2)

    val repository = mock<ProductRepository> {
        onBlocking { getProductsIn(productCategory) } doReturn products
    }

    @Nested
    @DisplayName("When view model is initialised")
    inner class Created {

        val tested = ProductCategoryViewModel(
            productCategory = productCategory,
            getProductsInCategory = GetProductsInCategory(repository),
            coroutineDispatchers = synchronousCoroutineDispatchers
        )

        @Test
        @DisplayName("refreshing is false")
        fun notRefreshing() {
            tested.refreshing.`should be false`()
        }

        @Test
        @DisplayName("productAdapterItems contains 6 placeholder adapter items")
        fun hasProductPlaceholderItems() {
            tested.productAdapterItems.apply {
                size `should be equal to` 6
                forEach { it `should be` ProductAdapterItem.buildWith(null) }
            }
        }

        @Nested
        @DisplayName("When refreshing set to true")
        inner class RefreshingSetTrue {

            @BeforeEach
            fun givenWhen() {
                tested.refreshing = true
            }

            @Test
            @DisplayName("productAdapterItems contains 3 adapter items with the expected products")
            fun hasProductItems() {
                tested.productAdapterItems.apply {
                    size `should be equal to` 2
                    (0..1).forEach { index ->
                        this[index] `should equal` ProductAdapterItem.buildWith(products[index])
                    }
                }
            }
        }
    }
}