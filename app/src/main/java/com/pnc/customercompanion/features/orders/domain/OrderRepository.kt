package com.pnc.customercompanion.features.orders.domain


interface OrderRepository {
    suspend fun getOrders(): List<Order>
}
