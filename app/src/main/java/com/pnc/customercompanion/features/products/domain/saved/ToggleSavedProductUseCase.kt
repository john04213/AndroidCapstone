package com.pnc.customercompanion.features.products.domain.saved

import com.pnc.customercompanion.features.products.domain.ProductItem
import com.pnc.customercompanion.features.products.domain.ProductRepository
import jakarta.inject.Inject

class ToggleSavedProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: ProductItem, isSaved: Boolean) {
        if (isSaved) repository.removeSavedProduct(product.id)
        else repository.saveProduct(product)
    }
}