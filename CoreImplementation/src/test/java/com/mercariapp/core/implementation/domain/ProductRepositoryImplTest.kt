package com.mercariapp.core.implementation.domain

import com.mercariapp.core.domain.Product
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.implementation.data.database.ProductCategoryDao
import com.mercariapp.core.implementation.data.database.ProductDao
import com.mercariapp.core.implementation.data.database.ProductDatabase
import com.mercariapp.core.implementation.data.database.RoomProduct
import com.mercariapp.core.implementation.data.database.RoomProductCategory
import com.mercariapp.core.implementation.data.network.ProductDto
import com.mercariapp.core.implementation.data.network.ProductCategoryDto
import com.mercariapp.core.implementation.data.network.ProductApiService
import com.mercariapp.testutils.deserializeToListWith
import com.mercariapp.testutils.stringResource
import com.mercariapp.testutils.synchronousCoroutineDispatchers
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyBlocking
import com.nhaarman.mockitokotlin2.whenever
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
import java.io.IOException

@ExperimentalCoroutinesApi
internal class ProductRepositoryImplTest {

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

    val productsCategories = productCategoriesFromApi.map { ProductCategory(it.name!!, it.dataEndpoint!!) }
    val allProducts = allProductsFromApi.map {
        Product(
            id = it.id!!,
            name = it.name!!,
            status = when (it.status) {
                "on_sale" -> Product.Status.ON_SALE
                "sold_out" -> Product.Status.SOLD_OUT
                else -> null
            }!!,
            likeCount = it.likeCount!!,
            commentCount = it.commentCount!!,
            priceInYen = it.priceInYen!!,
            photoUrl = it.photoUrl!!
        )
    }


    val roomProductCategories = productsCategories.map { RoomProductCategory(it.name, it.dataEndpoint) }

    val allRoomProducts = allProducts.map {
        RoomProduct(
            primaryKey = "${productsCategories.first().name}:${it.id}",
            id = it.id,
            name = it.name,
            status = when (it.status) {
                Product.Status.ON_SALE -> "on_sale"
                Product.Status.SOLD_OUT -> "sold_out"
            },
            likeCount = it.likeCount,
            commentCount = it.commentCount,
            priceInYen = it.priceInYen,
            photoUrl = it.photoUrl,
            categoryName = productsCategories.first().name
        )
    }

    val mockProductCategoryDao = mock<ProductCategoryDao> {
        onBlocking { getProductCategories() } doReturn roomProductCategories
    }

    val mockProductDao = mock<ProductDao> {
        onBlocking { getProductsInCategory(productsCategories.first().name) } doReturn allRoomProducts
    }

    val mockProductDatabase = mock<ProductDatabase> {
        on { productCategoryDao() } doReturn mockProductCategoryDao
        on { productDao() } doReturn mockProductDao
    }

    val mockProductApiService = mock<ProductApiService>()

    val tested = ProductRepositoryImpl(
        productService = mockProductApiService,
        productDatabase = mockProductDatabase,
        dispatchers = synchronousCoroutineDispatchers
    )

    private fun ProductApiService.prepareForSuccess() {
        runBlocking {
            whenever(getProductCategories()).thenReturn(productCategoriesFromApi)
            whenever(getProducts(productsCategories.first().dataEndpoint)).thenReturn(allProductsFromApi)
        }
    }

    private fun ProductApiService.prepareForFailure() {
        runBlocking {
            whenever(getProductCategories()).then { throw IOException() }
            whenever(getProducts(productsCategories.first().dataEndpoint)).then { throw IOException() }
        }
    }

    @Nested
    @DisplayName("When getProductCategories is called when a network connection is available")
    inner class GetProductCategoriesWithNetwork : GetProductCategories() {

        override fun setup() {
            mockProductApiService.prepareForSuccess()
        }

        @Test
        @DisplayName("Adds product categories to database")
        fun addsToDatabase() {
            verifyBlocking(mockProductCategoryDao) { addProductCategories(roomProductCategories) }
        }
    }

    @Nested
    @DisplayName("When getProductCategories is called when a network connection is not available")
    inner class GetProductCategoriesWithoutNetwork : GetProductCategories() {

        override fun setup() {
            mockProductApiService.prepareForFailure()
        }
    }

    abstract inner class GetProductCategories {

        lateinit var productCategories: List<ProductCategory>

        @BeforeEach
        fun givenWhen() {
            setup()
            productCategories = runBlocking { tested.getProductCategories() }
            if (productCategories.count() != productCategoriesFromApi.count()) {
                fail("The amount of product categories was inconsistent")
            }
        }

        abstract fun setup()

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
    @DisplayName("When getProductsIn category is called when a network connection is available")
    inner class GetMensProductsWithNetwork : GetMensProducts() {

        override fun setup() {
            mockProductApiService.prepareForSuccess()
        }

        @Test
        @DisplayName("Adds products to database")
        fun addsToDatabase() {
            verifyBlocking(mockProductDao) { addProducts(allRoomProducts) }
        }
    }

    @Nested
    @DisplayName("When getProductsIn category is called when a network connection is not available")
    inner class GetMensProductsWithoutNetwork : GetMensProducts() {

        override fun setup() {
            mockProductApiService.prepareForFailure()
        }
    }

    abstract inner class GetMensProducts {

        lateinit var products: List<Product>

        @BeforeEach
        fun givenWhen() {
            setup()
            products = runBlocking { tested.getProductsIn(productsCategories.first()) }
            if (products.count() != allProductsFromApi.count()) {
                fail("The amount of products was inconsistent")
            }
        }

        abstract fun setup()

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