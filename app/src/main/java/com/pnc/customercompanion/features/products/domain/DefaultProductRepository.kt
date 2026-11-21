package com.pnc.customercompanion.features.products.domain

import com.pnc.customercompanion.features.products.data.ProductDataSource
import com.pnc.customercompanion.features.products.data.ProductDetailMapper
import com.pnc.customercompanion.features.products.data.ProductMapper
import com.pnc.customercompanion.features.products.data.local.ProductItemDao
import com.pnc.customercompanion.features.products.data.local.ProductItemHistory
import com.pnc.customercompanion.features.products.data.local.ProductItemHistoryMapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val dataSource: ProductDataSource,
    private val productItemDao: ProductItemDao
): ProductRepository {
    override fun observeProducts(): Flow<List<Product>> = flow {
        emit(dataSource.getProducts().map { productDto ->
            ProductMapper.mapToDomain(productDto)
        })
    }

    override fun observeProduct(id: Int): Flow<ProductItem?> = flow {
        emit(ProductMapper.mapToDomain(dataSource.getProduct(id)))
    }

    override suspend fun refreshProducts() = Unit
    override suspend fun refreshProduct(id: Int) = Unit

    override suspend fun saveRecentProduct(productItem: ProductItemHistory) {
        productItemDao.insertOrUpdate(productItem)
    }

    override fun getProductHistory(): Flow<List<ProductItem>> {
        return productItemDao.getRecentProductHistory().map { productItems ->
            productItems.map { it }
        }
    }

    override suspend fun clearDatabase() {
        productItemDao.clearHistory()
    }
}