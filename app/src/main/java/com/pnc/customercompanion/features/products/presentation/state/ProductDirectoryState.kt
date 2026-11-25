package com.pnc.customercompanion.features.products.presentation.state

import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.features.products.domain.Product
import com.pnc.customercompanion.features.products.domain.SortOrder

data class ProductDirectoryState(
    val products: List<Product> = emptyList(),
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.NAME_ASC,
    val selectedProductId: Int? = null,
    val isRefreshing: Boolean = false,
    val loadFailure: LoadFailure? = null
) {
    val displayedProducts: List<Product>
        get() = products
            .filter {
                val matchesSearch = searchQuery == "" || it.name.contains(searchQuery, ignoreCase = true)

                matchesSearch
            }
            .let { filtered ->
                when (sortOrder) {
                    SortOrder.NAME_ASC -> filtered.sortedBy { it.name }
                    SortOrder.PRICE_ASC -> filtered.sortedBy { it.listPrice }
                    SortOrder.PRICE_DESC -> filtered.sortedByDescending { it.listPrice }
                }
            }

    val selectedProduct: Product?
        get() = products.find { it.id == selectedProductId }
}