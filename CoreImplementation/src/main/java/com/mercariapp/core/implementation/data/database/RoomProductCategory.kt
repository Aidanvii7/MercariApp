package com.mercariapp.core.implementation.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mercariapp.core.implementation.data.ProductCategory

@Entity(tableName = "product_categories")
internal data class RoomProductCategory(
    @PrimaryKey
    override val name: String,
    @ColumnInfo(name = "data_endpoint")
    override val dataEndpoint: String
) : ProductCategory