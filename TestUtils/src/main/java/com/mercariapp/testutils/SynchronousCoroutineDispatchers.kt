package com.mercariapp.testutils

import com.mercariapp.common.utils.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
val synchronousCoroutineDispatchers = CoroutineDispatchers(
    main = Dispatchers.Unconfined,
    default = Dispatchers.Unconfined,
    io = Dispatchers.Unconfined
)