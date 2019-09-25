package com.mercariapp.feature.browseproducts.domain

import com.mercariapp.core.domain.Product
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.core.domain.ProductRepository

internal class GetProductsInCategory(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productCategory: ProductCategory): List<Product> = repository.getProductsIn(productCategory)
}