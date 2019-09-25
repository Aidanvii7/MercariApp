package com.mercariapp.feature.common.presentation

import com.mercariapp.core.domain.Product

class ProductViewModel(val product: Product) {

    val name: String
        get() = product.name

    val imageUrl: String
        get() = product.photoUrl

    val likeCount: Int
        get() = product.likeCount

    val commentCount: Int
        get() = product.commentCount

    val price: Int
        get() = product.priceInYen

    val soldOut: Boolean
        get() = product.status == Product.Status.SOLD_OUT
}