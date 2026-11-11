package com.pnc.customercompanion.features.orders.domain

import java.math.BigDecimal
import java.time.LocalDateTime

//one product line of an order at a glance
data class OrderLines(
    val productName: String,
    val orderQty: Int,
    val unitPrice: BigDecimal,
    val lineTotal: BigDecimal
)

// one customer order
data class Order(
    val orderNumber: Int,
    val orderDate: LocalDateTime,
    val shipDate: LocalDateTime?,
    val statusCode: Int,
    val orderLines: List<OrderLines>
){
    val total: BigDecimal
        get() = orderLines.sumOf { it.lineTotal }
}