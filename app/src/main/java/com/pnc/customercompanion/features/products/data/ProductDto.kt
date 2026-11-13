package com.pnc.customercompanion.features.products.data

data class ProductDto (
    val productId: Int,
    val name: String,
    val productNumber: String,
    val thumbnailPhoto: String,
    val warranty: String?,
    val color: String?,
    val listPrice: Double,
    val stockLevel: Int
)