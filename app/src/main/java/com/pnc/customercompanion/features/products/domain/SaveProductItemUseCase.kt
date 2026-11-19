package com.pnc.customercompanion.features.products.domain

import com.pnc.customercompanion.features.products.data.local.ProductItemHistory
import javax.inject.Inject

class SaveProductItemUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productItem: ProductItemHistory) {
        repository.saveRecentProduct(productItem)
    }
}