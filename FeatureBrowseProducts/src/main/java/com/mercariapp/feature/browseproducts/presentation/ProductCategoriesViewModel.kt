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
import com.mercariapp.common.utils.RxSchedulers
import com.mercariapp.feature.browseproducts.domain.GetProductCategories
import com.mercariapp.feature.browseproducts.domain.GetProductsInCategory
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

internal class ProductCategoriesViewModel(
    private val getProductCategories: GetProductCategories,
    private val getProductsInCategory: GetProductsInCategory,
    @VisibleForTesting
    private val schedulers: RxSchedulers = RxSchedulers.DEFAULT
) : ObservableArchViewModel() {

    @get:Bindable
    var refreshing: Boolean by bindable(false)
        .onFirstAccess { refreshing = true }
        .doOnTrue {
            disposable = fetchAndBuildAdapterItems()
                .observeOn(schedulers.main)
                .subscribeBy(
                    onError = { /* TODO log error */ }
                ) { productCategoryAdapterItems ->
                    this.productCategoryAdapterItems = productCategoryAdapterItems
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

    private var disposable: Disposable? = null

    private fun fetchAndBuildAdapterItems(): Single<List<ProductCategoryAdapterItem>> =
        getProductCategories().observeOn(schedulers.computation)
            .map { productCategories ->
                productCategories.map { productCategory ->
                    ProductCategoryAdapterItem(
                        productCategory = productCategory,
                        getProductsInCategory = getProductsInCategory
                    )
                }
            }

    override fun onCleared() {
        disposable?.dispose()
        productCategoryAdapterItems = emptyList()
    }
}