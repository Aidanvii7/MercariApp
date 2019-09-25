package com.mercariapp.common.utils.logger

fun Any.logD(message: String, tag: String = "") =
    CompositeLogger.d(tag, "${javaClass.simpleName}: ${Thread.currentThread().name} $message")

fun Any.logE(message: String, tag: String = "") =
    CompositeLogger.e(tag, "${javaClass.simpleName}: ${Thread.currentThread().name} $message")