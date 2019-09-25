package com.mercariapp.core.implementation.mappers

import com.mercariapp.common.utils.logger.logE
import com.mercariapp.core.implementation.data.ProductDto
import com.mercariapp.core.implementation.data.ProductCategoryDto
import com.mercariapp.core.domain.Product
import com.mercariapp.core.domain.ProductCategory

internal fun List<ProductCategoryDto>.toProductCategories(): List<ProductCategory> {
    return mapNotNull { productCategoryDto ->
        try {
            ProductCategory(
                name = productCategoryDto.name!!,
                dataEndpoint = productCategoryDto.dataEndpoint!!
            )
        } catch (_: KotlinNullPointerException) {
            null
        }
    }

}

internal fun List<ProductDto>.toProducts(): List<Product> {
    return mapNotNull { productDto ->
        try {
            Product(
                id = productDto.id!!,
                name = productDto.name!!,
                status = productDto.status.toStatus()!!,
                likeCount = productDto.likeCount!!,
                commentCount = productDto.commentCount!!,
                priceInYen = productDto.priceInYen!!,
                photoUrl = productDto.photoUrl!!
            )
        } catch (_: KotlinNullPointerException) {
            null
        }
    }
}

private fun String?.toStatus(): Product.Status? = when (this) {
    "on_sale" -> Product.Status.ON_SALE
    "sold_out" -> Product.Status.SOLD_OUT
    else -> null
}