package com.mercariapp.core.domain

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class ProductCategory(
    val name: String,
    val dataEndpoint: String
) : Parcelable