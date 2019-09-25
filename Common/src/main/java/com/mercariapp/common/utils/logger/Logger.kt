package com.mercariapp.common.utils.logger

interface Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String)
}