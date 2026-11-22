package com.pnc.customercompanion.core.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pnc.customercompanion.features.products.data.local.ProductItemDao
import com.pnc.customercompanion.features.products.data.local.ProductItemHistory

@Database(
    entities = [
        ProductItemHistory::class
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productItemDao(): ProductItemDao
}
