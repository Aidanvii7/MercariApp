package com.mercariapp.core.implementation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomProductCategory::class, RoomProduct::class], version = 1, exportSchema = false)
internal abstract class ProductDatabase : RoomDatabase() {
    abstract fun productCategoryDao(): ProductCategoryDao
    abstract fun productDao(): ProductDao
}