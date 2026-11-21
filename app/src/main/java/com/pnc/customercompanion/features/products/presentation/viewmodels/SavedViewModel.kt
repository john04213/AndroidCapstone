package com.pnc.customercompanion.features.products.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.products.domain.saved.GetSavedProductsUseCase
import com.pnc.customercompanion.features.products.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    getSavedProducts: GetSavedProductsUseCase
) : ViewModel() {

    val uiState: StateFlow<LoadState<List<Product>>> = getSavedProducts()
        .map { products ->
            if (products.isEmpty()) LoadState.Empty else LoadState.Success(products)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LoadState.Loading)
}