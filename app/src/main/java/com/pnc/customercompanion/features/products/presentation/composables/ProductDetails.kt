package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.products.domain.ProductDetail
import com.pnc.customercompanion.features.products.presentation.viewmodels.ProductDetailsViewModel
import com.pnc.customercompanion.ui.theme.AndroidCapstoneTheme

@Composable
fun ProductDetails(
    productId: Int,
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
            val product = (state as LoadState.Success<ProductDetail>).data
            ProductDetailsContent(product = product)
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
