package com.mercariapp.feature.common.databinding

import androidx.databinding.BindingAdapter
import androidx.annotation.ColorInt
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@Suppress("ObjectPropertyName")
internal var SwipeRefreshLayout._refreshing: Boolean
    @InverseBindingAdapter(attribute = "refreshing", event = "refreshingAttrChanged")
    get() = isRefreshing
    @BindingAdapter("refreshing")
    set(value) {
        isRefreshing = value
    }

@BindingAdapter(
    "onRefresh",
    "refreshingAttrChanged", requireAll = false
)
internal fun SwipeRefreshLayout.bindOnRefreshListener(
    onRefreshListener: SwipeRefreshLayout.OnRefreshListener?,
    refreshingAttrChanged: InverseBindingListener?
) {
    if (refreshingAttrChanged != null) {
        setOnRefreshListener {
            onRefreshListener?.onRefresh()
            refreshingAttrChanged.onChange()
        }
    } else setOnRefreshListener(onRefreshListener)
}

@BindingAdapter("loaderArrowColor")
internal fun SwipeRefreshLayout.bindLoaderArrowColor(@ColorInt color: Int?) {
    color?.let { setColorSchemeColors(color) }
}

@BindingAdapter("loaderBackgroundColor")
internal fun SwipeRefreshLayout.bindLoaderBackgroundColor(@ColorInt color: Int?) {
    color?.let { setProgressBackgroundColorSchemeColor(color) }
}