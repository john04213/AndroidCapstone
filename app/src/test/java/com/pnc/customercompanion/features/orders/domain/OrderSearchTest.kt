package com.pnc.customercompanion.features.orders.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class
OrderSearchTest {
    private fun createOrder(order: Int): Order {
        return Order(
            orderNumber = order,
            orderDate = LocalDateTime.now(),
            shipDate = null,
            statusCode = 5,
            orderLines = emptyList()
        )
    }
    val orders = listOf(
        createOrder(75069),
        createOrder(74977),
        createOrder(51980),
    )

    @Test
    fun `part of an order number keeps only the orders that contain it`() {
        val result = searchOrders(orders, "750")

        assertEquals(listOf(orders[0]), result)
    }

    @Test
    fun `an empty search keeps every order`() {
        val result = searchOrders(orders, "")

        assertEquals(orders, result)
    }

    @Test
    fun `spaces around the search are ignored`() {
        val result = searchOrders(orders, " 750 ")

        assertEquals(listOf(orders[0]), result)
    }

    @Test
    fun `a search that matches nothing gives an empty list`() {
        val result = searchOrders(orders, "1234")

        assertEquals(emptyList<Order>(), result)
    }
}