package com.mercariapp.feature.common.ui

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

enum class Orientation(private val value: Int) {
    VERTICAL(RecyclerView.VERTICAL),
    HORIZONTAL(RecyclerView.HORIZONTAL);
    operator fun invoke() = value
}

fun createGridLayoutManager(
    context: Context,
    spanCount: Int,
    orientation: Orientation = Orientation.VERTICAL,
    reverseLayout: Boolean = false
) = GridLayoutManager(context, spanCount, orientation(), reverseLayout)