package com.mercariapp.core.implementation.mappers

import com.mercariapp.core.implementation.data.Product
import com.mercariapp.core.implementation.data.ProductCategory
import com.mercariapp.core.implementation.data.database.RoomProduct
import com.mercariapp.core.implementation.data.database.RoomProductCategory
import com.mercariapp.core.domain.Product as DomainProduct
import com.mercariapp.core.domain.ProductCategory as DomainProductCategory

internal fun List<ProductCategory>.toDomainProductCategories(): List<DomainProductCategory> {
    return mapNotNull { productCategory ->
        try {
            DomainProductCategory(
                name = productCategory.name!!,
                dataEndpoint = productCategory.dataEndpoint!!
            )
        } catch (_: KotlinNullPointerException) {
            null
        }
    }
}

internal fun List<DomainProductCategory>.toRoomProductCategories(): List<RoomProductCategory> {
    return map { domainProductCategory ->
        RoomProductCategory(
            name = domainProductCategory.name,
            dataEndpoint = domainProductCategory.dataEndpoint
        )
    }
}

internal fun List<Product>.toDomainProducts(): List<DomainProduct> {
    return mapNotNull { product ->
        try {
            DomainProduct(
                id = product.id!!,
                name = product.name!!,
                status = product.status.toDomainStatus()!!,
                likeCount = product.likeCount!!,
                commentCount = product.commentCount!!,
                priceInYen = product.priceInYen!!,
                photoUrl = product.photoUrl!!
            )
        } catch (_: KotlinNullPointerException) {
            null
        }
    }
}

internal fun List<DomainProduct>.toRoomProductsWith(categoryName: String): List<RoomProduct> {
    return map { domainProduct ->
        RoomProduct(
            primaryKey = "$categoryName:${domainProduct.id}",
            id = domainProduct.id,
            name = domainProduct.name,
            status = domainProduct.status.toStringStatus(),
            likeCount = domainProduct.likeCount,
            commentCount = domainProduct.commentCount,
            priceInYen = domainProduct.priceInYen,
            photoUrl = domainProduct.photoUrl,
            categoryName = categoryName
        )
    }
}

private fun String?.toDomainStatus(): DomainProduct.Status? = when (this) {
    "on_sale" -> DomainProduct.Status.ON_SALE
    "sold_out" -> DomainProduct.Status.SOLD_OUT
    else -> null
}

private fun DomainProduct.Status.toStringStatus(): String = when (this) {
    DomainProduct.Status.ON_SALE -> "on_sale"
    DomainProduct.Status.SOLD_OUT -> "sold_out"
    else -> throw IllegalArgumentException("no mapping for type: ${this.name}")
}