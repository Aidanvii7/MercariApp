package com.mercariapp.common.utils.logger

import android.util.Log

class AndroidLogger(val defaultTag: String = "") : Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag.let { if (it.isNotEmpty()) it else defaultTag }, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag.let { if (it.isNotEmpty()) it else defaultTag }, message)
    }
}