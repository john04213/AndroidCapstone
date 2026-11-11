package com.pnc.customercompanion.features.orders.domain

// Newest first, oldest first
enum class OrderSort {
    NEWEST_FIRST,
    OLDEST_FIRST
}

// sorts by date, same date goes by order number
fun sortOrder(orders: List<Order>, sort: OrderSort): List<Order> {
    return when (sort) {
        OrderSort.NEWEST_FIRST -> orders.sortedWith(compareByDescending<Order> {
            it.orderDate }.thenBy { it.orderNumber})
        OrderSort.OLDEST_FIRST -> orders.sortedWith(compareBy<Order> {
            it.orderDate }.thenBy { it.orderNumber })
    }
}
