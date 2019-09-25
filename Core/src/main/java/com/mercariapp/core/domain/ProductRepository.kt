package com.mercariapp.core.domain

interface ProductRepository {

    suspend fun getProductCategories(): List<ProductCategory>

    suspend fun getProductsIn(productCategory: ProductCategory): List<Product>
}