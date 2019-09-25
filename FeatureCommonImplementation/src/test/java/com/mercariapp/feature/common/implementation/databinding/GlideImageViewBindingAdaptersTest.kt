package com.mercariapp.feature.common.implementation.databinding


import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mercariapp.testutils.anyNullable
import com.mercariapp.testutils.mockViewOfType
import com.mercariapp.testutils.verifyNoMoreRealInteractions
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class GlideImageViewBindingAdaptersTest {

    val requestBuilder = mock<RequestBuilder<Drawable>>().apply {
        whenever(transition(any())).thenReturn(this)
        whenever(apply(any())).thenReturn(this)
    }

    val requestManager = mock<RequestManager>().apply {
        whenever(load(any<String>())).thenReturn(requestBuilder)
    }

    val requestOptions = mock<RequestOptions>().apply {
        whenever(placeholder(anyNullable<Drawable>())).thenReturn(this)
        whenever(diskCacheStrategy(any())).thenReturn(this)
        whenever(override(any(), any())).thenReturn(this)
    }

    private val tested = GlideImageViewBindingAdapters(
        glideWith = { requestManager },
        requestOptions = { requestOptions }
    )

    val imageView = mockViewOfType<ImageView>()


    private fun ImageView.bind(imageUrl: String?) {
        tested.apply { bind(imageUrl) }
    }

    @Nested
    @DisplayName("When bound with null imageUrl")
    inner class ImageUrlNull {

        @BeforeEach
        fun givenWhen() {
            imageView.bind(null)
        }

        @Test
        @DisplayName("nothing happens")
        fun noOp() {
            imageView.verifyNoMoreRealInteractions()
        }
    }

    @Nested
    @DisplayName("When bound with empty imageUrl")
    inner class ImageUrlEmpty {

        @BeforeEach
        fun givenWhen() {
            imageView.bind("")
        }

        @Test
        @DisplayName("nothing happens")
        fun noOp() {
            imageView.verifyNoMoreRealInteractions()
        }
    }

    @Nested
    @DisplayName("When bound with non empty imageUrl")
    inner class ImageUrlNotEmpty {

        val imageUrl = "https://dummyimage.com/400x400/000/fff?text=men1"
        @BeforeEach
        fun givenWhen() {
            imageView.bind(imageUrl)
        }

        @Test
        fun `loads image url with correct configuration and updated path`() {
            inOrder(requestManager, requestBuilder, requestOptions).apply {
                verify(requestManager).load(imageUrl)
                verify(requestBuilder).transition(any())
                verify(requestOptions).diskCacheStrategy(DiskCacheStrategy.DATA)
                verify(requestBuilder).into(imageView)
                verifyNoMoreInteractions()
            }
        }
    }
}