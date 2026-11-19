package com.pnc.customercompanion.features.products.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(id: Int): Flow<ProductItem?> {
        return productRepository.observeProduct(id)
    }
}