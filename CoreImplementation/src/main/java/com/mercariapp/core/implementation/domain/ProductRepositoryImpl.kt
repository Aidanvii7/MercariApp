package com.mercariapp.core.implementation.domain

import com.mercariapp.common.utils.CoroutineDispatchers
import com.mercariapp.common.utils.logger.logD
import com.mercariapp.core.domain.Product
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.domain.ProductRepository
import com.mercariapp.core.implementation.data.database.ProductDatabase
import com.mercariapp.core.implementation.data.network.ProductApiService
import com.mercariapp.core.implementation.mappers.toDomainProductCategories
import com.mercariapp.core.implementation.mappers.toDomainProducts
import com.mercariapp.core.implementation.mappers.toRoomProductCategories
import com.mercariapp.core.implementation.mappers.toRoomProductsWith
import kotlinx.coroutines.withContext

internal class ProductRepositoryImpl(
    private val productService: ProductApiService,
    private val productDatabase: ProductDatabase,
    private val dispatchers: CoroutineDispatchers
) : ProductRepository {

    override suspend fun getProductCategories(): List<ProductCategory> {
        return withContext(dispatchers.io) {
            try {
                this@ProductRepositoryImpl.logD("getProductCategories start")
                val dtoProductCategories = productService.getProductCategories()
                this@ProductRepositoryImpl.logD("getProductCategories succeeded")
                dtoProductCategories.toDomainProductCategories().also { domainProductCategories ->
                    val roomProductCategories = domainProductCategories.toRoomProductCategories()
                    productDatabase.productCategoryDao().addProductCategories(roomProductCategories)
                }
            } catch (e: Throwable) {
                this@ProductRepositoryImpl.logD("getProductCategories failed")
                val roomProductCategories = productDatabase.productCategoryDao().getProductCategories()
                return@withContext roomProductCategories.toDomainProductCategories()
            }
        }
    }

    override suspend fun getProductsIn(productCategory: ProductCategory): List<Product> {
        return withContext(dispatchers.io) {
            try {
                this@ProductRepositoryImpl.logD("getProductsIn: ${productCategory.name} start")
                val dtoProducts = productService.getProducts(productCategory.dataEndpoint)
                this@ProductRepositoryImpl.logD("getProductsIn: ${productCategory.name} succeeded")
                dtoProducts.toDomainProducts().also { domainProducts ->
                    val roomProducts = domainProducts.toRoomProductsWith(productCategory.name)
                    productDatabase.productDao().addProducts(roomProducts)
                }
            } catch (e: Throwable) {
                this@ProductRepositoryImpl.logD("getProductsIn: ${productCategory.name} failed")
                val roomProducts = productDatabase.productDao().getProductsInCategory(productCategory.name)
                return@withContext roomProducts.toDomainProducts()
            }
        }
    }
}


