package com.pnc.customercompanion.features.products.domain.saved

import com.pnc.customercompanion.features.products.domain.Product
import com.pnc.customercompanion.features.products.domain.ProductRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetSavedProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> = repository.getSavedProducts()
}