package com.mercariapp.core.implementation.data

import retrofit2.http.GET
import retrofit2.http.Url

internal interface ProductApiService {

    @GET("https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/master.json")
    suspend fun getProductCategories(): List<ProductCategoryDto>

    @GET
    suspend fun getProducts(@Url url: String): List<ProductDto>
}