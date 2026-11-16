package com.pnc.customercompanion.features.products.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.customercompanion.features.products.domain.GetProductsUseCase
import com.pnc.customercompanion.features.products.domain.RefreshProductsUseCase
import com.pnc.customercompanion.features.products.presentation.state.ProductDirectoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProductDirectoryViewModel @Inject constructor(
    private val getProducts: GetProductsUseCase,
    private val refreshProducts: RefreshProductsUseCase,
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
): ViewModel() {
    private val _uiState = MutableStateFlow(
        ProductDirectoryState()
    )

    val uiState: StateFlow<ProductDirectoryState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getProducts().collect { products ->
                _uiState.update { currentState ->
                    currentState.copy(
                        products = products
                    )
                }
            }
        }
    }

    fun loadProducts() {
        if (_uiState.value.isRefreshing) return

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isRefreshing = true,
                    errorMessage = null
                )
            }

            try {
                refreshProducts()
                _uiState.update { currentState ->
                    currentState.copy(
                        isRefreshing = false
                    )
                }
            }
            catch (e: CancellationException) {
                throw e
            }
            catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        errorMessage = e.message ?: "Couldn't refresh products"
                    )
                }
            }
        }
    }

    fun dismissError() {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessage = null
            )
        }
    }

    fun updateSearchQuery(query: String) {
        savedStateHandle[SEARCH_KEY] = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedProduct(productId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedProductId = productId
            )
        }
    }

    companion object {
        const val SEARCH_KEY = "searchQuery"
    }
}