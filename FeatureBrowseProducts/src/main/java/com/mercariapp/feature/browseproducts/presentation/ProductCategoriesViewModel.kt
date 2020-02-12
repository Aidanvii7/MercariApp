package com.mercariapp.feature.browseproducts.presentation

import androidx.annotation.VisibleForTesting
import androidx.databinding.Bindable
import com.aidanvii.toolbox.adapterviews.databinding.disposeAll
import com.aidanvii.toolbox.adapterviews.databinding.recyclerpager.BindingRecyclerPagerBinder
import com.aidanvii.toolbox.databinding.ObservableArchViewModel
import com.aidanvii.toolbox.databinding.bindable
import com.aidanvii.toolbox.delegates.observable.doOnNextWithPrevious
import com.aidanvii.toolbox.delegates.observable.doOnTrue
import com.aidanvii.toolbox.delegates.observable.onFirstAccess
import com.mercariapp.common.utils.CoroutineDispatchers
import com.mercariapp.common.utils.logger.logD
import com.mercariapp.feature.browseproducts.domain.GetProductCategories
import com.mercariapp.feature.browseproducts.domain.GetProductsInCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ProductCategoriesViewModel(
    private val getProductCategories: GetProductCategories,
    @VisibleForTesting
    private val coroutineDispatchers: CoroutineDispatchers = CoroutineDispatchers.DEFAULT
) : ObservableArchViewModel() {

    @get:Bindable
    var refreshing: Boolean by bindable(false)
        .onFirstAccess { refreshing = true }
        .doOnTrue {
            CoroutineScope(coroutineDispatchers.main + job).launch {
                try {
                    this@ProductCategoriesViewModel.logD("refreshing = true")
                    productCategoryAdapterItems = fetchAndBuildAdapterItems()
                    this@ProductCategoriesViewModel.logD("refreshing = false")
                    refreshing = false
                    return@launch
                } catch (e: Throwable) {
                    this@ProductCategoriesViewModel.logD("refreshing failed")
                    //TODO: log error
                }
                refreshing = false
            }
        }

    @get:Bindable
    var showTabs: Boolean by bindable(false)
        private set

    @get:Bindable
    var productCategoryAdapterItems by bindable<List<ProductCategoryAdapterItem>>(emptyList())
        .doOnNextWithPrevious { oldItems, _ -> oldItems?.disposeAll() }
        private set

    val binder = BindingRecyclerPagerBinder<ProductCategoryAdapterItem>(
        hasMultipleViewTypes = false
    )

    private val job = SupervisorJob()

    private suspend fun fetchAndBuildAdapterItems(): List<ProductCategoryAdapterItem> =
        withContext(coroutineDispatchers.default) {
            this@ProductCategoriesViewModel.logD("fetchAndBuildAdapterItems")
            getProductCategories().map { productCategory ->
                ProductCategoryAdapterItem(
                    productCategory = productCategory
                )
            }
        }

    override fun onCleared() {
        job.cancel()
        productCategoryAdapterItems = emptyList()
    }
}