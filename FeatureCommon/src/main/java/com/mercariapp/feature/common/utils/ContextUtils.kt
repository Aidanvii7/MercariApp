package com.mercariapp.feature.common.utils

import android.app.Activity
import android.content.Context
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
fun Context?.isNotActivity(): Boolean {
    contract { returns(false) implies (this@isNotActivity is Activity) }
    return this !is Activity
}