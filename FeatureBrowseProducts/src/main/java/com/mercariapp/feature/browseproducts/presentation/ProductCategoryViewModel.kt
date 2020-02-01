package com.mercariapp.feature.browseproducts.presentation

import androidx.annotation.VisibleForTesting
import androidx.databinding.Bindable
import com.aidanvii.toolbox.adapterviews.recyclerview.BindingRecyclerViewBinder
import com.aidanvii.toolbox.databinding.ObservableViewModel
import com.aidanvii.toolbox.databinding.bindable
import com.aidanvii.toolbox.delegates.observable.doOnTrue
import com.aidanvii.toolbox.delegates.observable.onFirstAccess
import com.mercariapp.common.utils.RxSchedulers
import com.mercariapp.common.utils.repeatedListOf
import com.mercariapp.core.domain.ProductCategory
import com.mercariapp.feature.browseproducts.R
import com.mercariapp.feature.browseproducts.domain.GetProductsInCategory
import com.mercariapp.feature.common.ui.createGridLayoutManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

internal class ProductCategoryViewModel(
    private val productCategory: ProductCategory,
    private val getProductsInCategory: GetProductsInCategory,
    @VisibleForTesting
    private val schedulers: RxSchedulers = RxSchedulers.DEFAULT
) : ObservableViewModel() {

    @get:Bindable
    var refreshing: Boolean by bindable(false)
        .onFirstAccess { refreshing = true }
        .doOnTrue {
            disposable = Observable.concat(
                listOf(
                    Observable.just(placeholders),
                    fetchAndBuildAdapterItems().toObservable()
                )
            )
                .observeOn(schedulers.main)
                .subscribeBy(
                    onError = { /* TODO log error */ }
                ) { productAdapterItems ->
                    this.productAdapterItems = productAdapterItems
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
        }
    )

    private var disposable: Disposable? = null

    private fun fetchAndBuildAdapterItems(): Single<List<ProductAdapterItem>> =
        getProductsInCategory(productCategory)
            .observeOn(schedulers.computation)
            .map { products ->
                products.map { product ->
                    ProductAdapterItem.buildWith(product)
                }
            }

    override fun onDisposed() {
        disposable?.dispose()
    }

    companion object {
        private val placeholders: List<ProductAdapterItem> = repeatedListOf(
            element = ProductAdapterItem.buildWith(null),
            count = 6
        )
    }
}