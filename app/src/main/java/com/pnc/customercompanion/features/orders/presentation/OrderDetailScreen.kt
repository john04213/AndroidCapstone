package com.pnc.customercompanion.features.orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.ui.FailedView
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.core.ui.LoadingView
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderLines
import com.pnc.customercompanion.features.orders.domain.orderStatusLabel
import com.pnc.customercompanion.ui.theme.LocalSpacing

// the order details screen: shows the view that matches the current load state
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderNumber: Int,
    onBack: () -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderNumber) {
        viewModel.loadOrder(orderNumber)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return to orders"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (val state = uiState) {
                LoadState.Loading -> LoadingView()
                LoadState.Empty -> FailedView(
                    failure = LoadFailure.NOT_FOUND,
                    onRetry = viewModel::retry,
                    onBack = onBack
                )
                is LoadState.Success -> OrderDetailContent(order = state.data)
                is LoadState.Failed -> FailedView(
                    failure = state.failure,
                    onRetry = viewModel::retry,
                    onBack = onBack
                )
            }
        }
    }
}

// one order: its number, dates, status, every product line and the total
@Composable
internal fun OrderDetailContent(order: Order) {
    LazyColumn(
        modifier = Modifier.padding(LocalSpacing.current.medium),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium)
    ) {
        item {
            Text(
                text = "Order # ${order.orderNumber}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )
        }
        item {
            Column(
                modifier = Modifier.semantics(mergeDescendants = true) {}
            ) {
                Text(text = "Order date: ${formatOrderDate(order.orderDate)}")
                Text(text = "Ship date: ${formatShipDate(order.shipDate)}")
                Text(text = "Status: ${orderStatusLabel(order.statusCode)}")
            }
        }
        item {
            Text(
                text = "Products",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics { heading() }
            )
        }
        items(items = order.orderLines) { line ->
            OrderLineCard(line = line)
        }
        item {
            Text(
                text = "Order total: ${formatMoney(order.total)}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// one product line: its name, quantity, unit price and line total
@Composable
private fun OrderLineCard(line: OrderLines) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {}
    ) {
        Column(
            modifier = Modifier.padding(LocalSpacing.current.medium)
        ) {
            Text(
                text = line.productName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Quantity: ${line.orderQty}")
            Text(text = "Unit price: ${formatMoney(line.unitPrice)}")
            Text(text = "Line total: ${formatMoney(line.lineTotal)}")
        }
    }
}
