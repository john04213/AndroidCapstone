package com.pnc.customercompanion.features.products.domain

import com.pnc.customercompanion.features.products.data.local.ProductItemDao
import com.pnc.customercompanion.features.products.data.local.ProductItemHistory
import javax.inject.Inject

class SaveProductItemUseCase @Inject constructor(
    private val productItemDao: ProductItemDao
) {
    suspend operator fun invoke(productItem: ProductItemHistory) {
        productItemDao.insertOrUpdate(productItem)
    }
}