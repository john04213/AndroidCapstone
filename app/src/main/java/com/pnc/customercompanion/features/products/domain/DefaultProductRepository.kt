package com.pnc.customercompanion.features.products.domain

import com.pnc.customercompanion.features.products.data.ProductDataSource
import com.pnc.customercompanion.features.products.data.ProductDetailMapper
import com.pnc.customercompanion.features.products.data.ProductMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val dataSource: ProductDataSource
): ProductRepository {
    override fun observeProducts(): Flow<List<Product>> = flow {
        emit(dataSource.getProducts().map { productDto ->
            ProductMapper.mapToDomain(productDto)
        })
    }

    override fun observeProduct(id: Int): Flow<ProductDetail?> = flow {
        emit(ProductDetailMapper.mapToDomain(dataSource.getProduct(id)))
    }

    override suspend fun refreshProducts() = Unit
    override suspend fun refreshProduct(id: Int) = Unit
}