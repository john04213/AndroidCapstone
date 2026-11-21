package com.pnc.customercompanion.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.customercompanion.features.products.domain.ClearDatabaseUseCase
import com.pnc.customercompanion.features.products.domain.GetProductItemsUseCase
import com.pnc.customercompanion.features.products.domain.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val productItemsUseCase: GetProductItemsUseCase,
    private val clearData: ClearDatabaseUseCase
): ViewModel() {
    private val _productItems = MutableStateFlow<List<ProductItem>>(emptyList())
    val productItems: StateFlow<List<ProductItem>> = _productItems.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productItemsUseCase().collect { productList ->
                _productItems.value = productList
            }
        }
    }

    fun onClearHistoryClicked() {
        viewModelScope.launch {
            clearData()
        }
    }
}