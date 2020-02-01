package com.mercariapp.feature.browseproducts.domain

import com.mercariapp.core.domain.Product
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.domain.ProductRepository
import io.reactivex.Single

internal class GetProductsInCategory(
    private val repository: ProductRepository
) {
    operator fun invoke(productCategory: ProductCategory): Single<List<Product>> = repository.getProductsIn(productCategory)
}