package com.mercariapp.common.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class CoroutineDispatchers(
    val main: CoroutineDispatcher = Dispatchers.Main,
    val default: CoroutineDispatcher = Dispatchers.Default,
    val io: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        val DEFAULT = CoroutineDispatchers()
    }
}