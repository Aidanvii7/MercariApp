package com.mercariapp.testutils

import org.mockito.Mockito
import org.mockito.internal.util.MockUtil

fun Any.isSpy() = MockUtil.isSpy(this)
fun Any.isMock() = MockUtil.isMock(this)
fun Any.reset() = Mockito.reset(this)
fun <T> T.spied(): T = Mockito.spy(this)!!
inline fun <reified T> anyNullable(): T = Mockito.nullable(T::class.java)