package com.mercariapp.feature.browseproducts.domain

import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.domain.ProductRepository

internal class GetProductCategories(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): List<ProductCategory> = repository.getProductCategories()
}