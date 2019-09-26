package com.mercariapp.core.implementation.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mercariapp.core.implementation.data.network.ProductCategoryDto

@Dao
internal interface ProductCategoryDao {

    @Query("select * FROM product_categories")
    suspend fun getProductCategories(): List<RoomProductCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductCategories(productCategories: List<RoomProductCategory>)
}