package com.pnc.customercompanion.features.orders.domain

fun orderStatusLabel(statusCode: Int): String {
    return when (statusCode) {
        1 -> "Processing"
        2 -> "Approved"
        3 -> "Backordered"
        4 -> "Rejected"
        5 -> "Shipped"
        6 -> "Cancelled"
        else -> "Unknown Status"
    }
}
