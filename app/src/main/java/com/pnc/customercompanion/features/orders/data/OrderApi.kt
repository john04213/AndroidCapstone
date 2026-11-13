package com.pnc.customercompanion.features.orders.data

import retrofit2.http.GET

// server's order endpoint
interface OrderApi{
    // every customer's rows, one row per product line
    @GET("Order/customer")
    suspend fun getOrderRows(): List<OrderDto>
}
