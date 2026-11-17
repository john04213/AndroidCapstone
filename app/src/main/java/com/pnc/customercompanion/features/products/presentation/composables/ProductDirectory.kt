package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import com.pnc.customercompanion.R
import com.pnc.customercompanion.features.products.domain.SortOrder
import com.pnc.customercompanion.features.products.presentation.state.ProductDirectoryState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDirectory(
    state: ProductDirectoryState,
    useTwoPaneLayout: Boolean,
    onSortOrderChange: (SortOrder) -> Unit = {},
    onBack: () -> Unit = {},
    onProductSelected: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
    onDismissError: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text("Products")
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Return to discover"
                            )
                        }
                    }
                )
                DockedSearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = state.searchQuery,
                            onQueryChange = onSearchQueryChange,
                            onSearch = {},
                            expanded = false,
                            onExpandedChange = {},
                            placeholder = {
                                Text("Search products")
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Search, contentDescription = null)
                            }
                        )
                    },
                    expanded = false,
                    onExpandedChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    content = {}
                )
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(
                            text = state.sortOrder.label
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand dropdown"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        SortOrder.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option.label) },
                                onClick = {
                                    onSortOrderChange(option)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${pluralStringResource(
                                id = R.plurals.item_count,
                                count = state.displayedProducts.size,
                                state.displayedProducts.size
                            )}${state.searchQuery?.takeIf { it.isNotBlank() }?.let { " for " } ?: ""}${state.searchQuery} · ${state.sortOrder.label}",        // Add in-stock filter when able
                        modifier = Modifier.weight(5f),
                        softWrap = true
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            onSortOrderChange(SortOrder.NAME_ASC)
                            onSearchQueryChange("")
                            // Add in-stock filter when able
                        }
                    ) {
                        Text("Clear all")
                    }
                }
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh
        ) {
            ProductList(
                products = state.displayedProducts,
                onProductSelected = onProductSelected,
                innerPadding = PaddingValues(0.dp)
            )
        }
    }
}