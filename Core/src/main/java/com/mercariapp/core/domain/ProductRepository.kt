package com.mercariapp.core.domain

import io.reactivex.Single

interface ProductRepository {

    fun getProductCategories(): Single<List<ProductCategory>>

    fun getProductsIn(productCategory: ProductCategory): Single<List<Product>>
}