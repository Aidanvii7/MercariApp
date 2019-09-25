package com.mercariapp.core.implementation.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ProductCategoryDto(
    val name: String?,
    @Json(name = "data")
    val dataEndpoint: String?
)