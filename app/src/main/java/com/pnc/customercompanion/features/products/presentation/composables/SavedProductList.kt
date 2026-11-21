package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnc.customercompanion.features.products.domain.Product
import com.pnc.customercompanion.features.products.presentation.composables.ProductCard

@Composable
fun SavedProductList(
    products: List<Product>,
    onProductSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = products,
            key = { product -> product.id }
        ) { product ->
            ProductCard(
                product = product,
                onClick = {
                    onProductSelected(product.id)
                }
            )
        }
    }
}