package com.mercariapp.common.utils.logger

import androidx.annotation.MainThread

object CompositeLogger : Logger {

    private val loggers = mutableSetOf<Logger>()

    @MainThread
    operator fun plusAssign(logger: Logger) {
        loggers.add(logger)
    }

    @MainThread
    operator fun minusAssign(logger: Logger) {
        loggers.remove(logger)
    }

    override fun d(tag: String, message: String) = loggers.forEach { it.d(tag, message) }
    override fun e(tag: String, message: String) = loggers.forEach { it.e(tag, message) }
}