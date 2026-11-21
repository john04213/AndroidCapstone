package com.pnc.customercompanion.features.products.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.products.data.ProductMapper.mapToDto
import com.pnc.customercompanion.features.products.data.local.ProductItemHistoryMapper.toHistory
import com.pnc.customercompanion.features.products.domain.GetProductUseCase
import com.pnc.customercompanion.features.products.domain.ProductItem
import com.pnc.customercompanion.features.products.domain.SaveProductItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProduct: GetProductUseCase,
    private val saveProduct: SaveProductItemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoadState<ProductItem>>(LoadState.Loading)
    val uiState: StateFlow<LoadState<ProductItem>> = _uiState.asStateFlow()

    private var loadJob: Job? = null
    private var loadedId: Int? = null

    fun loadProduct(id: Int) {
        val alreadyShown = loadJob?.isActive == true || _uiState.value is LoadState.Success
        if (id == loadedId && alreadyShown) return

        loadedId = id
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = LoadState.Loading
            try {
                val product = getProduct(id).first()
                if (product == null) {
                    _uiState.value = LoadState.Failed(LoadFailure.NOT_FOUND)
                } else {
                    _uiState.value = LoadState.Success(product)
                    recordProductVisit(product)
                }
            } catch (e: LoadException) {
                _uiState.value = LoadState.Failed(e.failure)
            }
        }
    }

    fun retry() {
        val id = loadedId ?: return
        loadedId = null
        loadProduct(id)
    }

    private suspend fun recordProductVisit(product: ProductItem) {
        try {
            saveProduct(toHistory(mapToDto(product)))
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
        }
    }
}