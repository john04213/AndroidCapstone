package com.pnc.customercompanion.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// holds what the orders screen should show
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository
): ViewModel()  {
    private val _uiState = MutableStateFlow<LoadState<List<Order>>>(LoadState.Loading)

    val uiState: StateFlow<LoadState<List<Order>>> = _uiState.asStateFlow()

    init{
        loadOrders()
    }

    fun loadOrders(){
        _uiState.value = LoadState.Loading
        viewModelScope.launch {
            _uiState.value  = fetchOrdersState()
        }
    }

    private suspend fun fetchOrdersState(): LoadState<List<Order>> {
        try{
            val orders = orderRepository.getOrders()
            return if(orders.isEmpty()) {
                LoadState.Empty
            } else{
                LoadState.Success(orders)
            }
        } catch (e: LoadException){
            return LoadState.Failed(e.failure)
        }
    }
}
