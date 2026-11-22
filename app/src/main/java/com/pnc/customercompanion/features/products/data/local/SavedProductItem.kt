package com.pnc.customercompanion.features.products.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_products")
data class SavedProductItem(
    @PrimaryKey val id: Int,
    val name: String,
    val productNumber: String,
    val photo: String,
    val color: String?,
    val listPrice: Double,
    val stockLevel: Int,
    val savedAt: Long = System.currentTimeMillis()
)