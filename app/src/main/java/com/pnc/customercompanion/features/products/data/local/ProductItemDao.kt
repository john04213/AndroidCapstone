package com.pnc.customercompanion.features.products.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(productItem: ProductItemHistory)

    @Query("SELECT * FROM product_browsing_history ORDER BY timestamp DESC LIMIT 10")
    fun getRecentProductHistory(): Flow<List<ProductItemHistory>>
}