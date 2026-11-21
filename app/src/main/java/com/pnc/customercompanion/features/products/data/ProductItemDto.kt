package com.pnc.customercompanion.features.products.data

import kotlinx.serialization.Serializable

@Serializable
data class ProductItemDto (
    val productId: Int,
    val productName: String,
    val productNumber: String,
    val photo: String,
    val color: String?,
    val listPrice: Double,
    val stockLevel: Int,
    val productLine: String?,
    val productModelId: Int,
    val productModelName: String,
    val manufacturer: String?,
    val material: String?,
    val style: String?,
    val numberOfSteps: Int,
    val setupHours: Int,
    val machineHours: Int,
    val laborHours: Int,
    val lotSize: Int
)