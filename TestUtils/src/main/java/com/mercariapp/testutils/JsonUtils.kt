package com.mercariapp.testutils

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

inline fun <reified T> String.deserializeToListWith(moshi: Moshi): List<T> {
    val parameterizedType = Types.newParameterizedType(List::class.java, T::class.java)
    val parameterizedTypeAdapter = moshi.adapter<List<T>>(parameterizedType)
    return parameterizedTypeAdapter.fromJson(this) ?: emptyList()
}