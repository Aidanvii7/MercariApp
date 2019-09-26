package com.mercariapp.core.implementation.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mercariapp.core.implementation.data.network.ProductDto

@Dao
internal interface ProductDao {

    @Query("select * FROM products WHERE category_name = :name")
    suspend fun getProductsInCategory(name: String): List<RoomProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProducts(products: List<RoomProduct>)
}