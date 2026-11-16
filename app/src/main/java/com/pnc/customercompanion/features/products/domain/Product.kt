package com.pnc.customercompanion.features.products.domain

data class Product(
    val id: Int,
    val name: String,
    val productNumber: String,
    val photo: String,
    val warranty: String?,
    val color: String?,
    val listPrice: Double,
    val stockLevel: Int
)