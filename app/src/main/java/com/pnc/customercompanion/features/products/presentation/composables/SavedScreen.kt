package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.core.ui.LoadingView
import com.pnc.customercompanion.features.products.presentation.viewmodels.SavedViewModel
import com.pnc.customercompanion.ui.theme.LocalSpacing

@Composable
fun SavedScreen(
    onProductSelected: (Int) -> Unit,
    onBrowseProducts: () -> Unit,
    viewModel: SavedViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(LocalSpacing.current.medium),
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium)
        ) {
            Text(
                text = "Saved products",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.semantics { heading() }
            )

            when (val current = state) {
                is LoadState.Success -> {
                    Column(
                    verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium)
                ) {
                    Text(
                        text = "${current.data.size} items saved",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    SavedProductList(
                        products = current.data,
                        onProductSelected = onProductSelected
                    )
                }
            }
                is LoadState.Empty -> SavedEmptyView(onBrowseProducts = onBrowseProducts)

                else -> LoadingView()
            }
        }
    }
}