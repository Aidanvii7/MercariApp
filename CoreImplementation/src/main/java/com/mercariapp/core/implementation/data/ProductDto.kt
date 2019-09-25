package com.mercariapp.core.implementation.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ProductDto(
    val id: String?,
    val name: String?,
    val status: String?,
    @Json(name = "num_likes")
    val likeCount: Int?,
    @Json(name = "num_comments")
    val commentCount: Int?,
    @Json(name = "price")
    val priceInYen: Int?,
    @Json(name = "photo")
    val photoUrl: String?
)