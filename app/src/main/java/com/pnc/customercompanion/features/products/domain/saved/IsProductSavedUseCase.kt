package com.pnc.customercompanion.features.products.domain.saved

import com.pnc.customercompanion.features.products.domain.ProductRepository
import jakarta.inject.Inject

class IsProductSavedUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Boolean = repository.isSaved(id)
}