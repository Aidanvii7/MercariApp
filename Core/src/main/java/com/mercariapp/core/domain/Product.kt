package com.mercariapp.core.domain

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Product(
    val id: String,
    val name: String,
    val status: Status,
    val likeCount: Int,
    val commentCount: Int,
    val priceInYen: Int,
    val photoUrl: String
) : Parcelable {

    @Keep
    @Parcelize
    enum class Status : Parcelable {
        ON_SALE,
        SOLD_OUT;
    }
}