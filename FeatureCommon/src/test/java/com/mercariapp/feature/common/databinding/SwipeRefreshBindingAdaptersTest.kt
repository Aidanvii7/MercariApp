package com.mercariapp.feature.common.databinding

import android.graphics.Color
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mercariapp.testutils.mockViewOfType
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.amshove.kluent.`should be false`
import org.amshove.kluent.`should be true`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class SwipeRefreshBindingAdaptersTest {

    val color = Color.MAGENTA
    val tested = mockViewOfType<SwipeRefreshLayout>()

    val mockOnRefreshListener = mock<SwipeRefreshLayout.OnRefreshListener>()
    val mockRefreshingAttrChanged = mock<InverseBindingListener>()

    @Nested
    @DisplayName("When refreshing")
    inner class Refreshing {

        @BeforeEach
        fun givenWhen() {
            whenever(tested.isRefreshing).thenReturn(true)
        }

        @Test
        @DisplayName("alias property _refreshing is true")
        fun aliasPropertyTrue() {
            tested._refreshing.`should be true`()
        }
    }

    @Nested
    @DisplayName("When not refreshing")
    inner class NotRefreshing {

        @BeforeEach
        fun givenWhen() {
            whenever(tested.isRefreshing).thenReturn(false)
        }

        @Test
        @DisplayName("alias property _refreshing is false")
        fun aliasPropertyTrue() {
            tested._refreshing.`should be false`()
        }
    }

    @Nested
    @DisplayName("When bindOnRefreshListener called with only OnRefreshListener")
    inner class OnlyOnRefreshListener {

        @BeforeEach
        fun givenWhen() {
            tested.bindOnRefreshListener(mockOnRefreshListener, null)
        }

        @Test
        @DisplayName("sets given OnRefreshListener")
        fun setsOnRefreshListener() {
            verify(tested).setOnRefreshListener(mockOnRefreshListener)
        }
    }

    @Nested
    @DisplayName("When bindOnRefreshListener called with only InverseBindingListener")
    inner class OnlyInverseBindingListener {

        @BeforeEach
        fun givenWhen() {
            tested.bindOnRefreshListener(null, mockRefreshingAttrChanged)
        }

        @Nested
        @DisplayName("When onRefresh is called on listener")
        inner class OnRefreshIsCalled {

            @BeforeEach
            fun givenWhen() {
                argumentCaptor<SwipeRefreshLayout.OnRefreshListener>().apply {
                    verify(tested).setOnRefreshListener(capture())
                    firstValue.onRefresh()
                }
            }

            @Test
            @DisplayName("calls given InverseBindingListener's onChange")
            fun callsInverseListener() {
                verify(mockRefreshingAttrChanged).onChange()
            }
        }
    }

    @Nested
    @DisplayName("When bindOnRefreshListener called with both OnRefreshListener and InverseBindingListener")
    inner class Both {

        @BeforeEach
        fun givenWhen() {
            tested.bindOnRefreshListener(mockOnRefreshListener, mockRefreshingAttrChanged)
        }

        @Nested
        @DisplayName("When onRefresh is called on listener")
        inner class OnRefreshIsCalled {

            @BeforeEach
            fun givenWhen() {
                argumentCaptor<SwipeRefreshLayout.OnRefreshListener>().apply {
                    verify(tested).setOnRefreshListener(capture())
                    firstValue.onRefresh()
                }
            }

            @Test
            @DisplayName("calls given OnRefreshListener's onRefresh")
            fun setsOnRefreshListener() {
                verify(mockOnRefreshListener).onRefresh()
            }

            @Test
            @DisplayName("calls given InverseBindingListener's onChange")
            fun callsInverseListener() {
                verify(mockRefreshingAttrChanged).onChange()
            }
        }
    }

    @Nested
    @DisplayName("When given loaderArrowColor Int is non-null")
    inner class GivenLoaderArrowColorNotNull {

        @BeforeEach
        fun givenWhen() {
            tested.bindLoaderArrowColor(color)
        }

        @Test
        @DisplayName("forwards to setColorSchemeColors")
        fun callsSetColorSchemeColors() {
            verify(tested).setColorSchemeColors(color)
        }
    }

    @Nested
    @DisplayName("When given loaderArrowColor Int is null")
    inner class GivenLoaderArrowColorNull {

        @BeforeEach
        fun givenWhen() {
            tested.bindLoaderArrowColor(null)
        }

        @Test
        @DisplayName("does not do anything")
        fun noInteractions() {
            verifyZeroInteractions(tested)
        }
    }

    @Nested
    @DisplayName("When given loaderBackgroundColor Int is non-null")
    inner class GivenLoaderBackgroundColorNotNull {

        @BeforeEach
        fun givenWhen() {
            tested.bindLoaderBackgroundColor(color)
        }

        @Test
        @DisplayName("forwards to setProgressBackgroundColorSchemeColor")
        fun callsSetProgressBackgroundColorSchemeColor() {
            verify(tested).setProgressBackgroundColorSchemeColor(color)
        }
    }

    @Nested
    @DisplayName("When given loaderBackgroundColor Int is null")
    inner class GivenLoaderBackgroundColorNull {

        @BeforeEach
        fun givenWhen() {
            tested.bindLoaderBackgroundColor(null)
        }

        @Test
        @DisplayName("does not do anything")
        fun noInteractions() {
            verifyZeroInteractions(tested)
        }
    }
}