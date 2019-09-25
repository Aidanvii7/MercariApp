package com.mercariapp.feature.common.databinding

import android.view.View
import com.mercariapp.testutils.mockView
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ViewVisibilityBindingAdaptersTest {

    val view = mockView()

    @Nested
    @DisplayName("When view.goneUnless(true)")
    inner class GoneUnlessTrue {

        @BeforeEach
        fun givenWhen() {
            view.goneUnless(true)
        }

        @Test
        @DisplayName("view is visible")
        fun viewVisible() {
            verify(view).visibility = View.VISIBLE
        }
    }

    @Nested
    @DisplayName("When view.goneUnless(false)")
    inner class GoneUnlessFalse {

        @BeforeEach
        fun givenWhen() {
            view.goneUnless(false)
        }

        @Test
        @DisplayName("view is gone")
        fun viewGone() {
            verify(view).visibility = View.GONE
        }
    }
}