package com.mercariapp.core.implementation.domain

import com.mercariapp.core.domain.Product
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.implementation.data.ProductDto
import com.mercariapp.core.implementation.data.ProductCategoryDto
import com.mercariapp.core.implementation.data.ProductApiService
import com.mercariapp.testutils.deserializeToListWith
import com.mercariapp.testutils.stringResource
import com.mercariapp.testutils.synchronousCoroutineDispatchers
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

@ExperimentalCoroutinesApi
internal class ProductRepositoryImplTest {

    companion object {
        const val allProductsEndpoint = "https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/all.json"
    }

    val moshi: Moshi by lazy {
        //TODO: use generated type adapters instead of reflection version
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    val productCategoriesFromApi: List<ProductCategoryDto>
        get() = stringResource("api/product_categories.json").deserializeToListWith(moshi)
    val allProductsFromApi: List<ProductDto>
        get() = stringResource("api/product_category_all.json").deserializeToListWith(moshi)

    val mockProductApiService = mock<ProductApiService> {
        onBlocking { getProductCategories() } doReturn productCategoriesFromApi
        onBlocking { getProducts(allProductsEndpoint) } doReturn allProductsFromApi
    }

    val tested = ProductRepositoryImpl(
        apiService = mockProductApiService,
        dispatchers = synchronousCoroutineDispatchers
    )

    @Nested
    @DisplayName("When getProductCategories is called")
    inner class GetProductCategories {

        lateinit var productCategories: List<ProductCategory>

        @BeforeEach
        fun givenWhen() {
            productCategories = runBlocking { tested.getProductCategories() }
            if (productCategories.count() != productCategoriesFromApi.count()) {
                fail("The amount of product categories was inconsistent")
            }
        }

        @Test
        @DisplayName("names are correct")
        fun mapsName() {
            testProductCategories { productCategoryDto, productCategory ->
                productCategory.name `should be equal to` productCategoryDto.name!!
            }
        }

        @Test
        @DisplayName("dataEndpoints are correct")
        fun mapsProductCategories() {
            testProductCategories { productCategoryDto, productCategory ->
                productCategory.dataEndpoint `should be equal to` productCategoryDto.dataEndpoint!!
            }
        }

        inline fun testProductCategories(test: (productCategoryDto: ProductCategoryDto, productCategory: ProductCategory) -> Unit) {
            for (i in 0 until productCategories.count()) {
                val productCategoryDto = productCategoriesFromApi[i]
                val productCategory = productCategories[i]
                test(productCategoryDto, productCategory)
            }
        }
    }

    @Nested
    @DisplayName("When getProductsIn is called")
    inner class GetMensProduct {

        lateinit var products: List<Product>

        @BeforeEach
        fun givenWhen() {
            products = runBlocking { tested.getProductsIn(ProductCategory("Men", allProductsEndpoint)) }
            if (products.count() != allProductsFromApi.count()) {
                fail("The amount of products was inconsistent")
            }
        }

        @Test
        @DisplayName("ids are correct")
        fun mapsId() {
            testProducts { productDto, product ->
                product.id `should be equal to` productDto.id!!
            }
        }

        @Test
        @DisplayName("names are correct")
        fun mapsName() {
            testProducts { productDto, product ->
                product.name `should be equal to` productDto.name!!
            }
        }

        @Test
        @DisplayName("statuses are correct")
        fun mapsStatus() {
            testProducts { productDto, product ->
                val expectedStatus = when (productDto.status) {
                    "on_sale" -> Product.Status.ON_SALE
                    "sold_out" -> Product.Status.SOLD_OUT
                    else -> null
                }
                product.status `should be` expectedStatus
            }
        }

        @Test
        @DisplayName("likeCounts are correct")
        fun mapsLikeCount() {
            testProducts { productDto, product ->
                product.likeCount `should be equal to` productDto.likeCount!!
            }
        }

        @Test
        @DisplayName("commentCounts are correct")
        fun mapsCommentCount() {
            testProducts { productDto, product ->
                product.commentCount `should be equal to` productDto.commentCount!!
            }
        }

        @Test
        @DisplayName("priceInYens are correct")
        fun mapsPriceInYen() {
            testProducts { productDto, product ->
                product.priceInYen `should be equal to` productDto.priceInYen!!
            }
        }

        @Test
        @DisplayName("photoUrls are correct")
        fun mapsPhotoUrl() {
            testProducts { productDto, product ->
                product.priceInYen `should be equal to` productDto.priceInYen!!
            }
        }

        inline fun testProducts(test: (productDto: ProductDto, product: Product) -> Unit) {
            for (i in 0 until products.count()) {
                val productDto = allProductsFromApi[i]
                val product = products[i]
                test(productDto, product)
            }
        }
    }

}