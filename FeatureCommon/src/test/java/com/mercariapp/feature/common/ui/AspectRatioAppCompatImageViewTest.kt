package com.mercariapp.feature.common.ui

import com.mercariapp.testutils.BeforeAndAfterEach
import com.mercariapp.testutils.mockViewOfType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension

internal class AspectRatioAppCompatImageViewTest {

    val widthMeasureSpec = 200
    val unusedHeightMeasureSpec = 200
    val tested = mockViewOfType<AspectRatioAppCompatImageView>().apply {
        whenever(onMeasure(eq(widthMeasureSpec), any())).thenCallRealMethod()
    }

    var currentAspectRatio = 1f
    val mockSetMeasuredDimensions = mock<AspectRatioAppCompatImageView.(width: Int, height: Int) -> Unit>()

    @JvmField
    @RegisterExtension
    val extension = Extension()

    @Nested
    @DisplayName("When aspectRatio is 1")
    inner class AspectRatio1 {

        @BeforeEach
        fun givenWhen() {
            currentAspectRatio = 1f
        }

        @Nested
        @DisplayName("When onMeasure called")
        inner class OnMeasure {

            @BeforeEach
            fun givenWhen() {
                tested.onMeasure(widthMeasureSpec, unusedHeightMeasureSpec)
            }

            @Test
            @DisplayName("calls setsMeasuredDimensions with same width and height")
            fun setsMeasuredDimensions() {
                tested.apply {
                    verify(mockSetMeasuredDimensions)(widthMeasureSpec, widthMeasureSpec)
                }
            }
        }
    }

    @Nested
    @DisplayName("When aspectRatio is 2")
    inner class AspectRatio2 {

        @BeforeEach
        fun givenWhen() {
            currentAspectRatio = 2f
        }

        @Nested
        @DisplayName("When onMeasure called")
        inner class OnMeasure {

            @BeforeEach
            fun givenWhen() {
                tested.onMeasure(widthMeasureSpec, unusedHeightMeasureSpec)
            }

            @Test
            @DisplayName("calls setsMeasuredDimensions with width and height = width / 2")
            fun setsMeasuredDimensions() {
                tested.apply {
                    verify(mockSetMeasuredDimensions)(widthMeasureSpec, widthMeasureSpec / 2)
                }
            }
        }
    }

    inner class Extension : BeforeAndAfterEach {
        override fun beforeEach(context: ExtensionContext?) {
            AspectRatioAppCompatImageView.getSizeFromMeasureSpec = { it }
            AspectRatioAppCompatImageView.getAspectRatio = { currentAspectRatio }
            AspectRatioAppCompatImageView.setMeasuredDimensions = mockSetMeasuredDimensions
        }

        override fun afterEach(context: ExtensionContext?) {
            AspectRatioAppCompatImageView.getSizeFromMeasureSpec = AspectRatioAppCompatImageView.defaultSetSizeFromMeasureSpec
            AspectRatioAppCompatImageView.getAspectRatio = AspectRatioAppCompatImageView.defaultGetAspectRatio
            AspectRatioAppCompatImageView.setMeasuredDimensions = AspectRatioAppCompatImageView.defaultSetMeasuredDimensions
        }
    }

}
