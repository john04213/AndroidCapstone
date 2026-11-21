package com.pnc.customercompanion.features.products.data

import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.error.toLoadFailure
import com.pnc.customercompanion.features.products.data.local.ProductItemDao
import com.pnc.customercompanion.features.products.data.local.ProductItemHistory
import com.pnc.customercompanion.features.products.domain.Product
import com.pnc.customercompanion.features.products.domain.ProductItem
import com.pnc.customercompanion.features.products.domain.ProductRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultProductRepository @Inject constructor(
    private val dataSource: ProductDataSource,
    private val productItemDao: ProductItemDao
): ProductRepository {
    override suspend fun observeProducts(): Flow<List<Product>> = flow {
        emit(dataSource.getProducts().map { productDto ->
            ProductMapper.mapToDomain(productDto)
        })
    }

    override suspend fun observeProduct(id: Int): Flow<ProductItem?> = flow {
        val productDto = try {
            dataSource.getProduct(id)
        } catch (error: CancellationException) {
            throw error
        } catch (error: LoadException) {
            throw error
        } catch (error: Exception) {
            throw (LoadException(toLoadFailure(error), error))
        }
        emit(ProductMapper.mapToDomain(productDto))
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