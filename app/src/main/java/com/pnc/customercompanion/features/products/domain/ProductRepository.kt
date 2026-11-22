package com.pnc.customercompanion.features.products.domain

import com.pnc.customercompanion.features.products.data.local.ProductItemHistory
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun observeProducts(): Flow<List<Product>>
    suspend fun observeProduct(id: Int): Flow<ProductItem?>

    suspend fun refreshProducts()
    suspend fun refreshProduct(id: Int)

    suspend fun saveRecentProduct(productItem: ProductItemHistory)

    fun getProductHistory(): Flow<List<ProductItem>>

    suspend fun clearDatabase()

    fun getSavedProducts(): Flow<List<Product>>

    suspend fun isSaved(id: Int): Boolean

    suspend fun saveProduct(product: ProductItem)

    suspend fun removeSavedProduct(id: Int)
}