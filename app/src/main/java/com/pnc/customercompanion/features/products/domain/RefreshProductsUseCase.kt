package com.pnc.customercompanion.features.products.domain

import javax.inject.Inject

class RefreshProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke() {
        repository.refreshProducts()
    }
}