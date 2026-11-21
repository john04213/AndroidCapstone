package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.ui.FailedView
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.core.ui.LoadingView
import com.pnc.customercompanion.features.products.domain.ProductItem
import com.pnc.customercompanion.features.products.presentation.viewmodels.ProductDetailsViewModel

@Composable
fun ProductDetails(
    productId: Int,
    onBack: (() -> Unit)? = null,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    when (val current = state) {
        is LoadState.Loading -> {
            LoadingView()
        }

        is LoadState.Empty -> FailedView(
            failure = LoadFailure.NOT_FOUND,
            onRetry = viewModel::retry,
            onBack = onBack
        )

        is LoadState.Success -> {
            val product = (state as LoadState.Success<ProductItem>).data
            ProductDetailsContent(
                product = product,
                onBack = onBack,
                isBookmarked = isSaved,
                onBookmarkClick = viewModel::toggleSaved
            )
        }

        is LoadState.Failed -> FailedView(
            failure = current.failure,
            onRetry = viewModel::retry,
            onBack = onBack
        )
    }
}
