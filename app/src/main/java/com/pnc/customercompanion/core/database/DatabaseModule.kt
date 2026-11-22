package com.pnc.customercompanion.core.database

import android.content.Context
import androidx.room.Room
import com.pnc.customercompanion.features.products.data.local.ProductItemDao
import com.pnc.customercompanion.features.products.data.local.SavedProductItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase): ProductItemDao {
        return database.productItemDao()
    }

    @Provides
    @Singleton
    fun provideSavedProductItemDao(database: AppDatabase): SavedProductItemDao {
        return database.savedProductItemDao()
    }
}