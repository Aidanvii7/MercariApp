@file:Suppress("IllegalIdentifier")

package com.mercariapp.testutils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atMost
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import de.jodamob.reflect.SuperReflect

class DummyViewTags {

    private val dummyViewTags = mutableMapOf<Int, Any>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(key: Int): T? = dummyViewTags[key] as T?

    operator fun <T : Any> set(key: Int, element: T?) {
        if (element != null) {
            dummyViewTags[key] = element
        } else dummyViewTags.remove(key)
    }
}

fun mockView() = mockViewOfType<View>()

inline fun <reified ViewType : View> mockViewOfType() = mock<ViewType>().apply { prepareForTest() }

tailrec fun <ViewType : View> ViewType.prepareForTest(
    context: Context? = null,
    resources: Resources? = null,
    dummyViewTags: DummyViewTags? = null
): ViewType = if (isSpy() or isMock()) apply {
    reset()
    val mockRootView = mock<View>().apply {
        whenever(viewTreeObserver).thenReturn(mock())
    }
    whenever(rootView).thenReturn(mockRootView)
    whenever(this.context).thenReturn(context ?: mock())
    whenever(this.resources).thenReturn(resources ?: mock())
    whenever(post(any())).thenAnswer { invocation -> (invocation.arguments[0] as java.lang.Runnable).run(); true }
    prepareWithDummyViewTags(dummyViewTags ?: DummyViewTags())
} else spied().prepareForTest()

private fun <V : View> V.prepareWithDummyViewTags(dummyViewTags: DummyViewTags) {
    pretendAtLeastIceCreamSandwich()
    whenever(getTag(anyNullable())).thenAnswer { invocation ->
        dummyViewTags[(invocation.arguments[0] as Int)]
    }
    whenever(setTag(any(), anyNullable())).thenAnswer { invocation ->
        dummyViewTags[(invocation.arguments[0] as Int)] = (invocation.arguments[1])
        Unit
    }
}

private fun pretendAtLeastIceCreamSandwich() {
    // required to take certain code path in the data binding framework:
    // https://android.googlesource.com/platform/frameworks/data-binding/+/master/extensions/baseAdapters/src/main/java/android/databinding/adapters/ListenerUtil.java#56
    SuperReflect.on(Build.VERSION::class.java).set("SDK_INT", Build.VERSION_CODES.ICE_CREAM_SANDWICH)
}

fun View.verifyNoMoreRealInteractions() {
    verify(this, atMost(Int.MAX_VALUE)).getTag(any())
    verify(this, atMost(Int.MAX_VALUE)).setTag(any(), anyNullable())
    verifyNoMoreInteractions(this)
}