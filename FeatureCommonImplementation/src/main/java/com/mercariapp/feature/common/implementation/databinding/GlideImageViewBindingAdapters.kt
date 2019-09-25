package com.mercariapp.feature.common.implementation.databinding

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import com.aidanvii.toolbox.Provider
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.mercariapp.common.utils.logger.logE
import com.mercariapp.feature.common.databinding.ImageViewBindingAdapters
import com.mercariapp.feature.common.utils.isNotActivity
import kotlin.contracts.ExperimentalContracts

typealias GlideWith = (context: Context) -> RequestManager

class GlideImageViewBindingAdapters(
    @param:VisibleForTesting
    private val glideWith: GlideWith = { Glide.with(it) },
    @param:VisibleForTesting
    private val requestOptions: Provider<RequestOptions> = { RequestOptions() }
) : ImageViewBindingAdapters() {

    private val crossFadeFactory = DrawableCrossFadeFactory
        .Builder()
        .setCrossFadeEnabled(true)
        .build()

    @ExperimentalContracts
    override fun ImageView.loadImage(imageUrl: String) {
        context.let { context ->
            if (context.isNotActivity() || !context.isDestroyed) {
                try {
                    glideWith(context)
                        .load(imageUrl)
                        .applyTransformations()
                        .into(this)
                } catch (e: IllegalArgumentException) {
                    logE(e.toString())
                }
            }
        }
    }

    @ExperimentalContracts
    override fun ImageView.cancelPendingRequest() {
        context.let { context ->
            if (context.isNotActivity() || !context.isDestroyed) {
                try {
                    glideWith(context).clear(this)
                } catch (e: IllegalArgumentException) {
                    logE(e.toString())
                }
            }
        }
    }

    private fun RequestBuilder<Drawable>.applyTransformations(): RequestBuilder<Drawable> =
        applyTransition().apply(requestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))

    private fun RequestBuilder<Drawable>.applyTransition(): RequestBuilder<Drawable> =
        transition(withCrossFade(crossFadeFactory))
}