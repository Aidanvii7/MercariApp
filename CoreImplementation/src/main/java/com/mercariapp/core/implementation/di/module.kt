package com.mercariapp.core.implementation.di

import com.mercariapp.common.utils.logger.logD
import com.mercariapp.core.implementation.data.ProductApiService
import com.mercariapp.core.domain.ProductRepository
import com.mercariapp.core.implementation.domain.ProductRepositoryImpl
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class CoreImplementationModuleConfig(
    val productRepositoryBaseUrl: String
)

val coreImplementationModule = module {

    factory {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }
    factory {
        OkHttpClient
            .Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    factory { KotlinJsonAdapterFactory() }
    factory {
        Moshi
            .Builder()
            .add(get<KotlinJsonAdapterFactory>())
            .build()
    }
    factory { MoshiConverterFactory.create(get()) }

    factory {
        Retrofit
            .Builder()
            .client(get())
            .baseUrl(get<CoreImplementationModuleConfig>().productRepositoryBaseUrl)
            .addConverterFactory(get<MoshiConverterFactory>())
            .build()
    }
    factory { get<Retrofit>().create(ProductApiService::class.java) }

    single<ProductRepository> {
        ProductRepositoryImpl(
            apiService = get(),
            dispatchers = get()
        )
    }
}