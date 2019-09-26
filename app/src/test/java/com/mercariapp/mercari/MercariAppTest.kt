package com.mercariapp.mercari

import android.app.Application
import com.mercariapp.core.domain.Product
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.testutils.parameterBindingsFor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules

@ExperimentalCoroutinesApi
internal class MercariAppTest {

    val appModules = MercariApp.appModules.toList()
    val stubModule = module {
        single {
            mock<Application> {
                on { applicationContext } doReturn mock()
            }
        }
    }

    @Nested
    @DisplayName("When building Koin dependency graph")
    inner class CheckModules {

        lateinit var koinApplication: KoinApplication

        @BeforeEach
        fun givenWhen() {
            Dispatchers.setMain(Dispatchers.Unconfined)
            koinApplication = koinApplication {
                modules(appModules + stubModule)
            }
        }

        @Test
        @DisplayName("All dependencies can be resolved")
        fun dependenciesResolved() {
            koinApplication.checkModules {
                parameterBindingsFor(
                    productCategoryViewModel,
                    ProductCategory(
                        name = "All",
                        dataEndpoint = "https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/all.json"
                    )
                )
                parameterBindingsFor(
                    productViewModel,
                    Product(
                        id = "onSaleProduct id",
                        name = "onSaleProduct name",
                        status = Product.Status.ON_SALE,
                        likeCount = 100,
                        commentCount = 200,
                        priceInYen = 300,
                        photoUrl = "https://dummyimage.com/400x400/000/fff?text=men1"
                    )
                )
            }
        }
    }

    private companion object {
        const val featureCommonPackage = "com.mercariapp.feature.common"
        const val featureBrowseProductsPackage = "com.mercariapp.feature.browseproducts"
        const val productCategoryViewModel = "$featureBrowseProductsPackage.presentation.ProductCategoryViewModel"
        const val productViewModel = "$featureCommonPackage.presentation.ProductViewModel"
    }
}