package com.pnc.customercompanion.features.products.domain

import com.pnc.customercompanion.features.products.data.HardCodedProductDataSource
import com.pnc.customercompanion.features.products.data.ProductDataSource
import com.pnc.customercompanion.features.products.data.ProductMapper
import kotlin.collections.map

class AsyncRepository(
    private val dataSource: ProductDataSource = HardCodedProductDataSource()
) {
    suspend fun getProducts(): List<Product> {
        return dataSource.getProducts()
            .map { productDto ->
                ProductMapper.mapToDomain(productDto)
            }
    }
}