package com.pnc.customercompanion.features.orders.data

import kotlinx.serialization.Serializable


@Serializable
data class OrderDto(
    val customerId: Int,
    val orderNumber: Int,
    val orderDate: String,
    val shipDate: String?,
    val status: Int,
    val productName: String,
    val orderQty: Int,
    val unitPrice: Double,
    val lineTotal: Double
)
