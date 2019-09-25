package com.mercariapp.feature.common.databinding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("goneUnless")
internal fun View.goneUnless(goneUnless: Boolean) {
    visibility = if (goneUnless) View.VISIBLE else View.GONE
}