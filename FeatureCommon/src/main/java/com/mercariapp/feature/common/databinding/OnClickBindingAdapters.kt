package com.mercariapp.feature.common.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import com.mercariapp.core.domain.Product

abstract class OnClickBindingAdapters {

    @BindingAdapter("onClickProduct")
    fun View.bindOnClickProduct(product: Product?) {
        setOnClickListener(
            product?.let { product ->
                View.OnClickListener { onClickProduct(product) }
            }
        )
    }

    abstract fun View.onClickProduct(product: Product)
}