package com.mercariapp.common.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
fun CharSequence?.isNotNullAndNotEmpty(): Boolean {
    contract { returns(true) implies (this@isNotNullAndNotEmpty != null) }
    return this != null && this.isNotEmpty()
}