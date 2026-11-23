package com.pnc.customercompanion.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// holds what the order details screen should show
@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoadState<Order>>(LoadState.Loading)
    val uiState: StateFlow<LoadState<Order>> = _uiState.asStateFlow()

    // the load running right now or nothing before the first one
    private var loadJob: Job? = null

    // the order number the screen asked for last
    private var loadedOrderNumber: Int? = null

    // loads one order; asking again for the order that is already showing or loading does nothing
    fun loadOrder(orderNumber: Int) {
        val alreadyShown = loadJob?.isActive == true || _uiState.value is LoadState.Success
        if (orderNumber == loadedOrderNumber && alreadyShown) return

        loadedOrderNumber = orderNumber
        loadJob?.cancel()
        _uiState.value = LoadState.Loading
        loadJob = viewModelScope.launch {
            try {
                val order = orderRepository.getOrder(orderNumber)
                if (order == null) {
                    _uiState.value = LoadState.Failed(LoadFailure.NOT_FOUND)
                } else {
                    _uiState.value = LoadState.Success(order)
                }
            } catch (e: LoadException) {
                _uiState.value = LoadState.Failed(e.failure)
            }
        }
    }

    // tries the same order again after a failure
    fun retry() {
        val orderNumber = loadedOrderNumber ?: return
        loadOrder(orderNumber)
    }
}
