package com.pnc.customercompanion.features.products.domain

class ProductDetail(
    val id: Int,
    val name: String,
    val productNumber: String,
    val thumbnailPhoto: String,
    val warranty: String?,
    val color: String?,
    val price: Double,
    val stockLevel: Int,
    val summary: String
)