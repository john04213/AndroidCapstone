package com.pnc.customercompanion.features.products.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(): Flow<List<Product>> {
        return productRepository
            .observeProducts()
            .map { products ->
                products
                    .sortedBy { it.name }
            }
    }
}