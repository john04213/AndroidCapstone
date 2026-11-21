package com.pnc.customercompanion.features.orders.domain

// keeps the orders whose number contains what the customer types and an empty search keeps every order
fun searchOrders(orders: List<Order>, searchQuery: String): List<Order>  {
    val typed = searchQuery.trim()
    if(typed.isEmpty()) return orders

    return orders.filter { order -> order.orderNumber.toString().contains(typed) }
}