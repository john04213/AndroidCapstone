package com.pnc.customercompanion.features.orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.customercompanion.core.ui.EmptyView
import com.pnc.customercompanion.core.ui.FailedView
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.core.ui.LoadingView
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.ui.theme.LocalSpacing
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

// the orders screen: shows the view that matches the current load state

@Composable
fun OrdersScreen(viewModel: OrdersViewModel  = hiltViewModel() ){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(LocalSpacing.current.large)
        ){
            Text(
                text = "My Orders",
                style = MaterialTheme.typography.titleLarge
            )
            when (val state = uiState) {
                LoadState.Loading -> LoadingView()
                LoadState.Empty -> EmptyView(message = "You have no orders yet")
                is LoadState.Success -> OrdersList(orders = state.data)
                is LoadState.Failed -> FailedView(failure = state.failure, onRetry = viewModel::loadOrders)
            }
        }
    }
}

@Composable
private fun OrdersList(orders: List<Order>){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium)
    ){
        items(items = orders, key = { order -> order.orderNumber }) { order ->
            OrderRow(order = order)
        }
    }
}

// one order: its' number, date, and total
@Composable
private fun OrderRow(order: Order){
    Card(
        modifier = Modifier
            .fillMaxWidth()
    )
        { Column(
            modifier = Modifier.padding(LocalSpacing.current.medium)
        ){
            Text(
                text = "Order # ${order.orderNumber}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Date: ${formatOrderDate(order.orderDate)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Total: ${formatMoney(order.total)}",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

private fun formatOrderDate(date: LocalDateTime): String {
    return date.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US))
}

private fun formatMoney(amount: BigDecimal): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}


