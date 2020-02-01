package com.mercariapp.core.implementation.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
internal interface ProductCategoryDao {

    @Query("select * FROM product_categories")
    fun getProductCategories(): Single<List<RoomProductCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProductCategories(productCategories: List<RoomProductCategory>): Completable
}