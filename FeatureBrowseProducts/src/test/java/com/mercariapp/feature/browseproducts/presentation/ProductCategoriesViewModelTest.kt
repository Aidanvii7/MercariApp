package com.mercariapp.feature.browseproducts.presentation

import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.domain.ProductRepository
import com.mercariapp.feature.browseproducts.domain.GetProductCategories
import com.mercariapp.feature.browseproducts.domain.GetProductsInCategory
import com.mercariapp.testutils.synchronousCoroutineDispatchers
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.`should be empty`
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should equal`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class ProductCategoriesViewModelTest {

    val productCategoryAll = ProductCategory(
        name = "All",
        dataEndpoint = "https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/all.json"
    )
    val productCategoryMen = ProductCategory(
        name = "Men",
        dataEndpoint = "https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/men.json"
    )
    val productCategoryWomen = ProductCategory(
        name = "Women",
        dataEndpoint = "https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/women.json"
    )
    val productCategories = listOf(productCategoryAll, productCategoryMen, productCategoryWomen)

    val repository = mock<ProductRepository> {
        onBlocking { getProductCategories() } doReturn productCategories
    }
    val getProductsInCategory = GetProductsInCategory(repository)

    @Nested
    @DisplayName("When view model is initialised")
    inner class Created {

        val tested = ProductCategoriesViewModel(
            getProductCategories = GetProductCategories(repository),
            getProductsInCategory = getProductsInCategory,
            coroutineDispatchers = synchronousCoroutineDispatchers
        )

        @Test
        @DisplayName("refreshing is false")
        fun notRefreshing() {
            tested.refreshing.`should be false`()
        }

        @Test
        @DisplayName("productCategoryAdapterItems is empty")
        fun hasProductPlaceholderItems() {
            tested.productCategoryAdapterItems.`should be empty`()
        }

        @Nested
        @DisplayName("When refreshing set to true")
        inner class RefreshingSetTrue {

            @BeforeEach
            fun givenWhen() {
                tested.refreshing = true
            }

            @Test
            @DisplayName("productCategoryAdapterItems contains 3 adapter items with the expected products")
            fun hasProductItems() {
                tested.productCategoryAdapterItems.apply {
                    size `should be equal to` 3
                    (0..1).forEach { index ->
                        this[index] `should equal` ProductCategoryAdapterItem(
                            productCategory = productCategories[index],
                            getProductsInCategory = getProductsInCategory
                        )
                    }
                }
            }
        }
    }
}