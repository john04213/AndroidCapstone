package com.pnc.customercompanion.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.pnc.customercompanion.features.products.data.local.ProductItemDao
import com.pnc.customercompanion.features.products.data.local.ProductItemHistory
import com.pnc.customercompanion.features.products.data.local.SavedProductItem
import com.pnc.customercompanion.features.products.data.local.SavedProductItemDao

@Database(
    entities = [
        ProductItemHistory::class,
        SavedProductItem::class
    ],
    version = 4,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 3, to = 4)
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productItemDao(): ProductItemDao
    abstract fun savedProductItemDao(): SavedProductItemDao
}
