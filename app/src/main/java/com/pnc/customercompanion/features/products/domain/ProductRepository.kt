package com.pnc.customercompanion.features.products.domain

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun observeProducts(): Flow<List<Product>>
    suspend fun observeProduct(id: Int): Flow<Product?>

    suspend fun refreshProducts()
    suspend fun refreshProduct(id: Int)
}