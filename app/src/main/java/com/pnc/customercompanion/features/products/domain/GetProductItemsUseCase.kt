package com.pnc.customercompanion.features.products.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetProductItemsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<ProductItem>> {
        return productRepository
            .getProductHistory()
            .map { products ->
                products
            }
    }
}