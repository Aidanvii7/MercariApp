package com.mercariapp.mercari

import android.app.Application
import com.mercariapp.common.utils.logger.AndroidLogger
import com.mercariapp.common.utils.logger.CompositeLogger
import com.mercariapp.core.implementation.di.coreImplementationModule
import com.mercariapp.feature.browseproducts.di.featureBrowseProductsModule
import com.mercariapp.feature.common.di.featureCommonModule
import com.mercariapp.feature.viewproduct.di.featureViewProductModule
import com.mercariapp.mercari.databinding.AppDataBindingComponent
import com.mercariapp.mercari.di.appModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class MercariApp : Application() {

    private val dataBindingComponent: AppDataBindingComponent by inject()

    override fun onCreate() {
        super.onCreate()
        initLogger()
        initKoin()
        initDataBindingComponent()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            CompositeLogger += AndroidLogger(defaultTag = "MercariApp")
        }
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MercariApp)
            modules(*appModules)
        }
    }

    private fun initDataBindingComponent() {
        dataBindingComponent.makeDefaultComponent()
    }

    companion object {
        val appModules: Array<Module>
            get() = arrayOf(
                appModule,
                featureBrowseProductsModule,
                featureViewProductModule,
                featureCommonModule,
                coreImplementationModule
            )
    }
}