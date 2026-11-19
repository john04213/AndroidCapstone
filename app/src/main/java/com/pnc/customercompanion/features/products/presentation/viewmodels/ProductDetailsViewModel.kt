package com.pnc.customercompanion.features.products.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.core.ui.LoadState.Success
import com.pnc.customercompanion.features.products.data.ProductMapper.mapToDto
import com.pnc.customercompanion.features.products.data.local.ProductItemDao
import com.pnc.customercompanion.features.products.data.local.ProductItemHistory
import com.pnc.customercompanion.features.products.data.local.ProductItemHistoryMapper.toHistory
import com.pnc.customercompanion.features.products.domain.GetProductUseCase
import com.pnc.customercompanion.features.products.domain.ProductItem
import com.pnc.customercompanion.features.products.domain.RefreshProductUseCase
import com.pnc.customercompanion.features.products.domain.SaveProductItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProduct: GetProductUseCase,
    private val saveProduct: SaveProductItemUseCase,
    private val refreshProduct: RefreshProductUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<LoadState<ProductItem>> = MutableStateFlow(
        LoadState.Loading
    )

    val uiState: StateFlow<LoadState<ProductItem>> = _uiState.asStateFlow()

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = LoadState.Loading
            try {
                val productItem = getProduct(productId).filterNotNull().first()
                productItem.let { prod ->
                    _uiState.value = Success(prod)
                }

                recordProductVisit(productItem)
            }
            catch (e: LoadException) {
                _uiState.value = LoadState.Failed(e.failure)
            }
        }
    }

    private suspend fun recordProductVisit(productItem: ProductItem) {
        val prodDto = mapToDto(productItem)
        val hist = toHistory(prodDto)
        saveProduct(hist)
    }
}