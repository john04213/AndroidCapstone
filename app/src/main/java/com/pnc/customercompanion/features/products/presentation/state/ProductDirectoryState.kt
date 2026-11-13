package com.pnc.customercompanion.features.products.presentation.state

import com.pnc.customercompanion.features.products.domain.Product

data class ProductDirectoryState(
    val products: List<Product> = emptyList(),
    val searchQuery: String = "",
    val selectedProductId: Int? = null,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
) {
    val displayedProducts: List<Product>
        get() = products.filter {
            val matchesSearch = searchQuery == "" || it.name.contains(searchQuery, ignoreCase = true)

            matchesSearch
        }

    val selectedProduct: Product?
        get() = products.find { it.id == selectedProductId }
}