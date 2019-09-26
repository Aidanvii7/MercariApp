package com.mercariapp.core.implementation.data.network

import com.mercariapp.core.implementation.data.ProductCategory
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ProductCategoryDto(
    override val name: String?,
    @Json(name = "data")
    override val dataEndpoint: String?
) : ProductCategory