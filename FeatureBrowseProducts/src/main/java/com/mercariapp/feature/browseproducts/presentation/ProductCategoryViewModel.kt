package com.mercariapp.feature.browseproducts.presentation

import androidx.annotation.VisibleForTesting
import androidx.databinding.Bindable
import com.aidanvii.toolbox.adapterviews.recyclerview.BindingRecyclerViewBinder
import com.aidanvii.toolbox.databinding.ObservableViewModel
import com.aidanvii.toolbox.databinding.bindable
import com.aidanvii.toolbox.delegates.observable.doOnTrue
import com.aidanvii.toolbox.delegates.observable.onFirstAccess
import com.mercariapp.common.utils.CoroutineDispatchers
import com.mercariapp.common.utils.logger.logD
import com.mercariapp.common.utils.repeatedListOf
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.feature.browseproducts.R
import com.mercariapp.feature.browseproducts.domain.GetProductsInCategory
import com.mercariapp.feature.common.ui.createGridLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ProductCategoryViewModel(
    private val productCategory: ProductCategory,
    private val getProductsInCategory: GetProductsInCategory,
    @VisibleForTesting
    private val coroutineDispatchers: CoroutineDispatchers = CoroutineDispatchers.DEFAULT
) : ObservableViewModel() {

    @get:Bindable
    var refreshing: Boolean by bindable(false)
        .onFirstAccess { refreshing = true }
        .doOnTrue {
            CoroutineScope(coroutineDispatchers.main + job).launch {
                try {
                    this@ProductCategoryViewModel.logD("refreshing = true for: ${productCategory.name} start")
                    productAdapterItems = placeholders
                    productAdapterItems = fetchAndBuildAdapterItems()
                    this@ProductCategoryViewModel.logD("refreshing = false for: ${productCategory.name} succeeded")
                    refreshing = false
                    return@launch
                } catch (e: Throwable) {
                    this@ProductCategoryViewModel.logD("refreshing failed for: ${productCategory.name} failed")
                    //TODO: log error
                }
                refreshing = false
            }
        }

    @get:Bindable
    var productAdapterItems: List<ProductAdapterItem> by bindable(placeholders)
        private set

    val binder = BindingRecyclerViewBinder<ProductAdapterItem>(
        layoutManagerFactory = { context ->
            createGridLayoutManager(
                context = context,
                spanCount = context.resources.getInteger(R.integer.overview_span_count)
            )
        },
        uiDispatcher = coroutineDispatchers.main,
        workerDispatcher = coroutineDispatchers.default
    )

    private val job = SupervisorJob()

    private suspend fun fetchAndBuildAdapterItems(): List<ProductAdapterItem> =
        withContext(coroutineDispatchers.default) {
            this@ProductCategoryViewModel.logD("fetchAndBuildAdapterItems for: ${productCategory.name}")
            getProductsInCategory(productCategory).map { ProductAdapterItem.buildWith(it) }
        }

    override fun onDisposed() {
        job.cancel()
    }

    companion object {
        private val placeholders: List<ProductAdapterItem> = repeatedListOf(
            element = ProductAdapterItem.buildWith(null),
            count = 6
        )
    }
}