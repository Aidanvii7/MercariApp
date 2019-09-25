package com.mercariapp.feature.common.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.aidanvii.toolbox.databinding.trackValue
import com.mercariapp.common.utils.isNotNullAndNotEmpty
import com.mercariapp.feature.common.R
import kotlin.contracts.ExperimentalContracts

abstract class ImageViewBindingAdapters {

    @BindingAdapter("imageUrl")
    @ExperimentalContracts
    fun ImageView.bind(imageUrl: String?) {
        trackValue(
            newValue = imageUrlFor(imageUrl),
            valueResId = R.id.image_view_url,
            onNewValue = { loadImage(it) },
            onOldValue = {
                cancelPendingRequest()
                if (imageUrl == null) {
                    setImageDrawable(null)
                }
            }
        )
    }

    @ExperimentalContracts
    private fun imageUrlFor(imageUrl: String?): String? =
        if (imageUrl.isNotNullAndNotEmpty()) imageUrl else null

    protected abstract fun ImageView.loadImage(imageUrl: String)

    protected abstract fun ImageView.cancelPendingRequest()
}