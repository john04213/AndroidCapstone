package com.pnc.customercompanion.features.orders.presentation

import com.pnc.customercompanion.features.orders.domain.OrderSort
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class OrdersScreenTest {
    @Test
    fun `an order date is shown as month day and year`() {
        val text = formatOrderDate(LocalDateTime.parse("2014-06-29T00:00:00"))
        assertEquals("Jun 29, 2014", text)
    }

    @Test
    fun `every sort has its own label`() {
        val labels = OrderSort.entries.map {sort -> sortLabel(sort) }
        assertEquals(OrderSort.entries.size, labels.toSet().size)
    }

    @Test
    fun `a total is shown as dollars with two decimals`() {
        val text = formatMoney(BigDecimal("1234.5"))
        assertEquals("$1,234.50", text)
    }
}