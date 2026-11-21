package com.pnc.customercompanion.features.orders.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.session.DemoCustomer
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderRepository
import com.pnc.customercompanion.features.orders.domain.OrderSort
import com.pnc.customercompanion.features.orders.domain.searchOrders
import com.pnc.customercompanion.features.orders.domain.sortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// holds what the orders screen should show
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    demoCustomer: DemoCustomer = DemoCustomer()
): ViewModel()  {

    // the demo customer's name shown under the title
    val customerName: String = demoCustomer.name
    private val _uiState = MutableStateFlow<LoadState<List<Order>>>(LoadState.Loading)

    val uiState: StateFlow<LoadState<List<Order>>> = _uiState.asStateFlow()

    // order list is shown,newest first until the customer picks another
    private val _sort = MutableStateFlow(OrderSort.NEWEST_FIRST)
    val sort: StateFlow<OrderSort> = _sort.asStateFlow()

    // true while a pull to refresh is running, so the screen can show progress over the list
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    // why the refresh failed, or nothing when it worked
    private val _refreshFailure = MutableStateFlow<LoadFailure?>(null)
    val refreshFailure: StateFlow<LoadFailure?> = _refreshFailure.asStateFlow()

    // what the customer typed in the search box
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    //every order from the last load, before the search and the sort are applied
    private var loadedOrders: List<Order> = emptyList()

    // the load running right now or nothing before the first one
    private var loadJob: Job? = null

    init{
        loadOrders()
    }

    fun loadOrders(){
         if(isLoadRunning()) return

        _uiState.value = LoadState.Loading
        loadJob = viewModelScope.launch {
            _uiState.value  = fetchOrdersState()
        }
    }

    //pull to refresh: loads again while the current list stays on screen
    fun refresh(){
        if(isLoadRunning()) return
        _isRefreshing.value= true
        _refreshFailure.value = null
        loadJob = viewModelScope.launch {
            showRefreshResult(fetchOrdersState())
            _isRefreshing.value = false
        }
    }

    //shows the loaded orders in a new order without asking the server again
    fun changeSort(newSort: OrderSort){
        _sort.value = newSort
        showVisibleOrders()
    }

    // shows only the loaded orders whose number contains the typed text without asking the server again
    fun changeSearch(typed: String){
        _searchQuery.value = typed
        showVisibleOrders()
    }

    // true while a first load, a retry or a refresh has not finished yet
    private fun isLoadRunning():Boolean {
        return loadJob?.isActive == true
    }

    // puts the searched and sorted on screen and does nothing while no list is showing
    private fun showVisibleOrders(){
        if(_uiState.value is LoadState.Success){
            _uiState.value = LoadState.Success(visibleOrders())
        }
    }

    // the loaded orders that match the search in the chosen order
    private fun visibleOrders(): List<Order>{
        return sortOrder(searchOrders(loadedOrders, _searchQuery.value), _sort.value)
    }

    // a failed refresh keeps the list that is on screen, any other result replaces it
    private fun showRefreshResult(state: LoadState<List<Order>>){
        if(state is LoadState.Failed && _uiState.value is LoadState.Success){
            _refreshFailure.value = state.failure
        } else{
            _uiState.value = state
        }
    }

    private suspend fun fetchOrdersState(): LoadState<List<Order>> {
        try{
            val orders = orderRepository.getOrders()
            return if(orders.isEmpty()) {
                LoadState.Empty
            } else{
                loadedOrders = orders
                LoadState.Success(visibleOrders())
            }
        } catch (e: LoadException){
            return LoadState.Failed(e.failure)
        }
    }
}
