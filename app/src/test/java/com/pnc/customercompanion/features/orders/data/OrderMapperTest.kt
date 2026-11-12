package com.pnc.customercompanion.features.orders.data

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class OrderMapperTest {
    // build one API row, a test only the parts it cares about
    private fun createRow(
        customerId: Int,
        orderNumber: Int,
        lineTotal: Double,
        shipDate: String? = "2014-07-29T00:00:00"
    ): OrderDto{
        return OrderDto(
            customerId = customerId,
            orderNumber = orderNumber,
            orderDate = "2014-06-29T00:00:00",
            shipDate = shipDate,
            status = 5,
            productName = "Touring Tire Tube",
            orderQty = 1,
            unitPrice = lineTotal,
            lineTotal = lineTotal
        )
    }

    @Test
    fun `four rows of one order become one order with an exact total`(){
        val rows = listOf(
            createRow(11502,75069,4.99),
            createRow(11502,75069,49.99),
            createRow(11502,75069,8.99),
            createRow(11502,75069,34.99)
        )

        val result = mapToOrders(rows, 11502)

        assertEquals(1, result.size)
        assertEquals(4, result[0].orderLines.size)
        assertEquals(BigDecimal("98.96"), result[0].total)
        assertEquals(LocalDateTime.of(2014, 7, 29, 0, 0), result[0].shipDate)
    }

    @Test
    fun `rows of another customer are left out`() {
        val rows = listOf(
            createRow(11794,75087,34.99),
            createRow(11502,75069,4.99)
        )

        val result = mapToOrders(rows, 11502)

        assertEquals(1, result.size)
        assertEquals(75069, result[0].orderNumber)
    }

    @Test
    fun `rows with a different order numbers become separate orders`() {
        val rows = listOf(
            createRow(11502,75069,4.99),
            createRow(11502,74977,49.99)
        )

        val result = mapToOrders(rows, 11502)

        assertEquals(2, result.size)
    }

    @Test
    fun `a missing ship date stays empty`() {
        val rows = listOf(
            createRow(11502,75069,4.99, null)
        )

        val result = mapToOrders(rows, 11502)

        assertEquals(null, result[0].shipDate)
    }
}