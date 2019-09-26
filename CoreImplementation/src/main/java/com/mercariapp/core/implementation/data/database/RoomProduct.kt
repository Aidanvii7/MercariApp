package com.mercariapp.core.implementation.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mercariapp.core.implementation.data.Product

@Entity(tableName = "products")
internal data class RoomProduct(
    @PrimaryKey
    val primaryKey: String,
    override val id: String,
    override val name: String,
    override val status: String,
    @ColumnInfo(name = "like_count")
    override val likeCount: Int,
    @ColumnInfo(name = "comment_count")
    override val commentCount: Int,
    @ColumnInfo(name = "price_in_yen")
    override val priceInYen: Int,
    @ColumnInfo(name = "photo_url")
    override val photoUrl: String,
    @ColumnInfo(name = "category_name")
    override val categoryName: String
) : Product