package com.mercariapp.common.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

data class RxSchedulers(
    val main: Scheduler = AndroidSchedulers.mainThread(),
    val computation: Scheduler = Schedulers.computation(),
    val io: Scheduler = Schedulers.io()
) {

    companion object {
        val DEFAULT = RxSchedulers()
    }
}