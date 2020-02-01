package com.mercariapp.feature.browseproducts.domain

import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.domain.ProductRepository
import io.reactivex.Single

internal class GetProductCategories(
    private val repository: ProductRepository
) {
    operator fun invoke(): Single<List<ProductCategory>> = repository.getProductCategories()
}