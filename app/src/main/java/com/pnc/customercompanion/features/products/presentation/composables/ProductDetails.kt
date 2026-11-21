package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.products.domain.ProductItem
import com.pnc.customercompanion.features.products.presentation.viewmodels.ProductDetailsViewModel

@Composable
fun ProductDetails(
    productId: Int,
    showBackArrow: Boolean = true,
    onBack: () -> Unit = {},
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    when (state) {
        is LoadState.Loading -> {
            CircularProgressIndicator()
        }

        is LoadState.Empty -> {
            Text("Product not found")
        }

        is LoadState.Success -> {
            val product = (state as LoadState.Success<ProductItem>).data
            ProductDetailsContent(product = product,
                onBack = onBack,
                showBackArrow = showBackArrow,
                onBookmarkClick = {}
            )
        }

        is LoadState.Failed -> {
            val message = (state as LoadState.Failed).failure
            Text(
                message.toString(),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
