package com.mercariapp.core.implementation.domain

import com.mercariapp.common.utils.CoroutineDispatchers
import com.mercariapp.common.utils.logger.logD
import com.mercariapp.core.domain.Product
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.domain.ProductRepository
import com.mercariapp.core.implementation.data.ProductApiService
import com.mercariapp.core.implementation.mappers.toProductCategories
import com.mercariapp.core.implementation.mappers.toProducts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class ProductRepositoryImpl(
    private val apiService: ProductApiService,
    private val dispatchers: CoroutineDispatchers
) : ProductRepository {

    override suspend fun getProductCategories(): List<ProductCategory> {
        return withContext(dispatchers.io) {
            try {
                this@ProductRepositoryImpl.logD("getProductCategories start")
                val apiProductCategories = apiService.getProductCategories()
                this@ProductRepositoryImpl.logD("getProductCategories succeeded")
                apiProductCategories.toProductCategories()
            } catch (e: Throwable) {
                this@ProductRepositoryImpl.logD("getProductCategories failed")
                throw e
            }
        }
    }

    override suspend fun getProductsIn(productCategory: ProductCategory): List<Product> {
        return withContext(dispatchers.io) {
            try {
                this@ProductRepositoryImpl.logD("getProductsIn: ${productCategory.name} start")
                val apiProducts = apiService.getProducts(productCategory.dataEndpoint)
                this@ProductRepositoryImpl.logD("getProductsIn: ${productCategory.name} succeeded")
                apiProducts.toProducts()
            } catch (e: Throwable) {
                this@ProductRepositoryImpl.logD("getProductsIn: ${productCategory.name} failed")
                throw e
            }
        }
    }
}


