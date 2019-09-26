package com.mercariapp.core.implementation.data.network

import com.mercariapp.core.implementation.data.Product
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ProductDto(
    override val id: String?,
    override val name: String?,
    override val status: String?,
    @Json(name = "num_likes")
    override val likeCount: Int?,
    @Json(name = "num_comments")
    override val commentCount: Int?,
    @Json(name = "price")
    override val priceInYen: Int?,
    @Json(name = "photo")
    override val photoUrl: String?,
    override val categoryName: String?
) : Product