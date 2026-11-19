package com.pnc.customercompanion.features.orders.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.ui.EmptyView
import com.pnc.customercompanion.core.ui.FailedView
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.core.ui.LoadingView
import com.pnc.customercompanion.core.ui.loadFailureMessage
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderSort
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
    val sort by viewModel.sort.collectAsStateWithLifecycle()
    val refreshFailure by viewModel.refreshFailure.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(LocalSpacing.current.medium),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.large)
        ){
            Text(
                text = "My Orders",
                style = MaterialTheme.typography.titleLarge
            )
            when (val state = uiState) {
                LoadState.Loading -> LoadingView()
                LoadState.Empty -> EmptyView(message = "You have no orders yet")
                is LoadState.Success -> OrdersList(
                    orders = state.data,
                    sort = sort,
                    onSortChange = viewModel::changeSort,
                    refreshFailure = refreshFailure,
                    isRefreshing = isRefreshing,
                    onRefresh = viewModel::refresh
                )
                is LoadState.Failed -> FailedView(failure = state.failure, onRetry = viewModel::loadOrders)
            }
        }
    }
}

// the sort chips with the scrolling list of orders under them
@Composable
private fun OrdersList(
    orders: List<Order>,
    sort: OrderSort,
    onSortChange: (OrderSort) -> Unit,
    refreshFailure: LoadFailure?,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium)
    ) {
        SortChips(
            selected = sort,
            onSortChange = onSortChange
        )
        if (refreshFailure != null) {
            RefreshFailureNote(failure = refreshFailure)
        }
            PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh) {
                key(sort) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium)
                    ) {
                        items(items = orders, key = { order -> order.orderNumber }) { order ->
                            OrderRow(order = order)
                    }
                }
            }
        }
    }
}


//one chip per sort order; the chosen one is highlighted
@Composable
private fun SortChips(selected: OrderSort, onSortChange: (OrderSort) -> Unit){
    Row( horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)){
        OrderSort.entries.forEach { sort ->
            FilterChip(
                selected = selected == sort,
                onClick = { onSortChange(sort) },
                label = { Text(text = sortLabel(sort)) }
            )
        }
    }
}

@Composable
private fun RefreshFailureNote(failure: LoadFailure){
    Text(
        text = "Couldn't refresh. ${loadFailureMessage(failure)}",
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium
    )
}

// the word shown on a sort chip
internal fun sortLabel(sort: OrderSort): String {
    return when (sort) {
        OrderSort.NEWEST_FIRST -> "Newest First"
        OrderSort.OLDEST_FIRST -> "Oldest First"
    }
}

// one order: it's number, date, and total
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

internal fun formatOrderDate(date: LocalDateTime): String {
    return date.format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US))
}

internal fun formatMoney(amount: BigDecimal): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}


