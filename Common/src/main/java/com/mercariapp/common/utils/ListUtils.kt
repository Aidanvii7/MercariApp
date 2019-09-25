package com.mercariapp.common.utils

fun <T> repeatedListOf(element: T, count: Int): List<T> =
    mutableListOf<T>().run {
        repeat(count) { add(element) }
        toList()
    }