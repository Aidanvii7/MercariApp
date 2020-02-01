package com.mercariapp.mercari.di

import com.mercariapp.common.utils.RxSchedulers
import com.mercariapp.core.implementation.di.CoreImplementationModuleConfig
import com.mercariapp.feature.common.implementation.databinding.GlideImageViewBindingAdapters
import com.mercariapp.feature.viewproduct.presentation.ViewProductFragment
import com.mercariapp.mercari.BuildConfig
import com.mercariapp.mercari.databinding.AppDataBindingComponent
import com.mercariapp.mercari.databinding.AppOnClickBindingAdapters
import org.koin.dsl.module

val appModule = module {
    single { RxSchedulers.DEFAULT }
    single {
        CoreImplementationModuleConfig(
            productRepositoryBaseUrl = BuildConfig.PRODUCT_CATEGORIES_URL
        )
    }
    single {
        AppDataBindingComponent(
            onClickBindingAdapters = AppOnClickBindingAdapters(),
            imageViewBindingAdapters = GlideImageViewBindingAdapters()
        )
    }

    single<ViewProductFragment.Arguments.FromBundle> { ViewProductFragmentArgumentsFromBundle() }
}