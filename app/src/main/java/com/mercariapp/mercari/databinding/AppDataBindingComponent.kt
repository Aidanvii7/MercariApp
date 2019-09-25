package com.mercariapp.mercari.databinding

import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.mercariapp.feature.common.databinding.ImageViewBindingAdapters
import com.mercariapp.feature.common.databinding.OnClickBindingAdapters

data class AppDataBindingComponent(
    private val onClickBindingAdapters: OnClickBindingAdapters,
    private val imageViewBindingAdapters: ImageViewBindingAdapters
) : DataBindingComponent {
    fun makeDefaultComponent() {
        DataBindingUtil.setDefaultComponent(this)
    }

    override fun getOnClickBindingAdapters() = onClickBindingAdapters
    override fun getImageViewBindingAdapters() = imageViewBindingAdapters
}