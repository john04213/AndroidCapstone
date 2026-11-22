package com.pnc.customercompanion.features.products.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedProductItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(product: SavedProductItem)

    @Query("DELETE FROM saved_products WHERE id = :id")
    suspend fun remove(id: Int)

    @Query("SELECT * FROM saved_products ORDER BY savedAt DESC")
    fun getSavedProducts(): Flow<List<SavedProductItem>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_products WHERE id = :id)")
    suspend fun isSaved(id: Int): Boolean
}