package com.mercariapp.core.implementation.domain

import com.mercariapp.common.utils.RxSchedulers
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
import io.reactivex.Single

internal class ProductRepositoryImpl(
    private val productService: ProductApiService,
    private val productDatabase: ProductDatabase,
    private val schedulers: RxSchedulers
) : ProductRepository {

    override fun getProductCategories(): Single<List<ProductCategory>> {
        return productService.getProductCategories()
            .subscribeOn(schedulers.io)
            .map { productCategories -> productCategories.toDomainProductCategories() }
            .flatMap { domainProductCategories ->
                val roomProductCategories = domainProductCategories.toRoomProductCategories()
                productDatabase.productCategoryDao().addProductCategories(roomProductCategories)
                    .blockingAwait()
                Single.just(domainProductCategories)
            }
            .doOnError { error -> logD("ProductRepository", "getProductCategories error: $error") }
            .onErrorResumeNext(
                productDatabase.productCategoryDao().getProductCategories()
                    .map { roomProductCategories ->
                        roomProductCategories.toDomainProductCategories()
                    }
            )
    }

    override fun getProductsIn(productCategory: ProductCategory): Single<List<Product>> {
        return productService.getProducts(productCategory.name)
            .subscribeOn(schedulers.io)
            .map { products -> products.toDomainProducts() }
            .flatMap { domainProducts ->
                val roomProducts = domainProducts.toRoomProductsWith(productCategory.name)
                productDatabase.productDao().addProducts(roomProducts).blockingAwait()
                Single.just(domainProducts)
            }
            .doOnError { error -> logD("ProductRepository", "getProducts error: $error") }
            .onErrorResumeNext(
                productDatabase.productDao().getProductsInCategory(productCategory.name)
                    .map { roomProducts ->
                        roomProducts.toDomainProducts()
                    }
            )
    }
}


