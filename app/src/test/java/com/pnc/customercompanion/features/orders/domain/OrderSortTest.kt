package com.pnc.customercompanion.features.orders.domain

import java.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Test


class OrderSortTest {
    private fun createOrder(orderNumber: Int, orderDate: LocalDateTime): Order {
        return Order(
            orderNumber = orderNumber,
            orderDate = LocalDateTime.parse(orderDate.toString()),
            shipDate = null,
            statusCode = 5,
            orderLines = emptyList()
        )
    }

    @Test
    fun `newest first puts the latest order on top `(){
        val older = createOrder(1, LocalDateTime.of(2023, 2, 1, 0, 0))
        val newest = createOrder(2, LocalDateTime.of(2023, 3, 1, 0, 0))
        val oldest = createOrder(4, LocalDateTime.of(2023, 1, 1, 0, 0))

        val result = sortOrder(listOf(older, newest, oldest), OrderSort.NEWEST_FIRST)
        assertEquals(listOf(newest, older, oldest), result)
    }

    @Test
    fun `oldest first puts earlier order on top`() {
        val older = createOrder(1, LocalDateTime.of(2023, 2, 1, 0, 0))
        val newest = createOrder(2, LocalDateTime.of(2023, 3, 1, 0, 0))
        val oldest = createOrder(4, LocalDateTime.of(2023, 1, 1, 0, 0))

    val result = sortOrder(listOf(newest, older, oldest), OrderSort.OLDEST_FIRST)

        assertEquals(listOf(oldest, older, newest), result)
    }

    @Test
    fun `orders with same date are sorted by order number`() {
        val first = createOrder(1, LocalDateTime.of(2023, 2, 1, 0, 0))
        val second = createOrder(2, LocalDateTime.of(2023, 2, 1, 0, 0))

        val newest = sortOrder(listOf(second, first), OrderSort.NEWEST_FIRST)
        val oldest = sortOrder(listOf(second, first), OrderSort.OLDEST_FIRST)

        assertEquals(listOf(first, second), newest)
        assertEquals(listOf(first, second), oldest)
    }
}