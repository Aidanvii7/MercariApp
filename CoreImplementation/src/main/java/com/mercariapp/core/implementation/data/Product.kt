package com.mercariapp.core.implementation.data

internal interface Product {
    val id: String?
    val name: String?
    val status: String?
    val likeCount: Int?
    val commentCount: Int?
    val priceInYen: Int?
    val photoUrl: String?
    val categoryName: String?
}