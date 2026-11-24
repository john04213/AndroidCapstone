package com.pnc.customercompanion.features.products.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.features.products.domain.GetProductsUseCase
import com.pnc.customercompanion.features.products.domain.RefreshProductsUseCase
import com.pnc.customercompanion.features.products.domain.SortOrder
import com.pnc.customercompanion.features.products.presentation.state.ProductDirectoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    fun loadProducts() {
        if (_uiState.value.isRefreshing) return

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isRefreshing = true
                )
            }

            try {
                refreshProducts()
                getProducts().collect { productsList ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            products = productsList
                        )
                    }
                }
                _uiState.update { currentState ->
                    currentState.copy(
                        isRefreshing = false
                    )
                }
            }
            catch (e: LoadException) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isRefreshing = false,
                        loadFailure = e.failure
                    )
                }
            }
        }
    }

    fun updateSortOrderChange(sort: SortOrder) {
        _uiState.update { currentState ->
            currentState.copy(
                sortOrder = sort
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