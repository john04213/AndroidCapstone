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
import com.pnc.customercompanion.features.products.domain.GetProductsUseCase
import com.pnc.customercompanion.features.products.domain.Product
import com.pnc.customercompanion.features.products.domain.ProductItem
import com.pnc.customercompanion.features.products.domain.RefreshProductUseCase
import com.pnc.customercompanion.features.products.domain.RefreshProductsUseCase
import com.pnc.customercompanion.core.error.toLoadFailure
import com.pnc.customercompanion.features.products.domain.ProductDetail
import com.pnc.customercompanion.features.products.domain.SaveProductItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
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

    private var loadJob: Job? = null
    private var loadedId: Int? = null

    fun loadProduct(id: Int) {
        // Don't restart (and flash loading) if this id is already being observed
        if (id == loadedId && loadJob?.isActive == true) return

        loadedId = id
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = LoadState.Loading
            val hadCache = getProduct(id).first() != null
            var visitRecorded = false

            // refresh the product data in the background
            launch {
                try {
                    refreshProduct(id)
                }
                catch (e: CancellationException) {
                    throw e
                }
                catch (e: Exception) {
                    // with a cached copy on screen, a failed refresh is silent
                    if (!hadCache) {
                        _uiState.value = LoadState.Failed(
                            toLoadFailure(e)
                        )
                    }
                }
            }

            // establish our subscription
            getProduct(id).collect { product ->
                when {
                    product != null -> {
                        _uiState.value = LoadState.Success(product)
                        // added: save to history once per visit
                        if (!visitRecorded) {
                            visitRecorded = true
                            recordProductVisit(product)
                        }
                    }
                    hadCache -> _uiState.value = LoadState.Empty // temporary state while the refresh is still underway
                    // no other useful cases - stay in loading state
                    // until the refresh either writes to the db or fails
                }
            }
        }
    }

    private suspend fun recordProductVisit(productItem: ProductItem) {
        val prodDto = mapToDto(productItem)
        val hist = toHistory(prodDto)
        saveProduct(hist)
    }
}

}

