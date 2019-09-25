package com.mercariapp.feature.common.ui

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.AppCompatImageView

class AspectRatioAppCompatImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var aspectRatio: Float = 1f
        set(value) {
            if (value > 0f) {
                field = value
            }
        }

    @VisibleForTesting
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getSizeFromMeasureSpec(widthMeasureSpec)
        val height = (width / getAspectRatio()).toInt()
        setMeasuredDimensions(width, height)
    }

    companion object {

        @VisibleForTesting
        val defaultSetSizeFromMeasureSpec: (Int) -> Int = { MeasureSpec.getSize(it) }

        @VisibleForTesting
        var getSizeFromMeasureSpec: (Int) -> Int = defaultSetSizeFromMeasureSpec

        @VisibleForTesting
        val defaultGetAspectRatio: AspectRatioAppCompatImageView.() -> Float = { this.aspectRatio }

        @VisibleForTesting
        var getAspectRatio: AspectRatioAppCompatImageView.() -> Float = defaultGetAspectRatio

        @VisibleForTesting
        val defaultSetMeasuredDimensions: AspectRatioAppCompatImageView.(width: Int, height: Int) -> Unit = { width, height ->
            setMeasuredDimension(width, height)
        }

        @VisibleForTesting
        var setMeasuredDimensions: AspectRatioAppCompatImageView.(width: Int, height: Int) -> Unit = defaultSetMeasuredDimensions
    }
}

