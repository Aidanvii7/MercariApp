package com.mercariapp.core.implementation.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mercariapp.core.implementation.data.network.ProductDto
import io.reactivex.Completable
import io.reactivex.Single

@Dao
internal interface ProductDao {

    @Query("select * FROM products WHERE category_name = :name")
    fun getProductsInCategory(name: String): Single<List<RoomProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProducts(products: List<RoomProduct>): Completable
}