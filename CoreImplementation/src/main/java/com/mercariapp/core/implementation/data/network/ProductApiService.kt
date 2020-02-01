package com.mercariapp.core.implementation.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

internal interface ProductApiService {

    @GET("https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/master.json")
    fun getProductCategories(): Single<List<ProductCategoryDto>>

    @GET
    fun getProducts(@Url url: String): Single<List<ProductDto>>
}