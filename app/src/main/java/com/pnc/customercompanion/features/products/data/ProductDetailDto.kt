package com.pnc.customercompanion.features.products.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailDto (
    @SerialName("productId") val productId: Int,
    @SerialName("productName") val name: String,
    val productNumber: String,
    @SerialName("photo") val thumbnailPhoto: String = "",
    @SerialName("warrantyDescription") val warranty: String? = null,
    val color: String? = null,
    val listPrice: Double = 0.0,
    val stockLevel: Int,
    val summary: String
)