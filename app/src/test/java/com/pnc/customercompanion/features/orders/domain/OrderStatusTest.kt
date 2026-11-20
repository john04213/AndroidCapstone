package com.pnc.customercompanion.features.orders.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class OrderStatusTest {
    @Test
    fun `each Known status code has its own words`() {
        val labels = (1..6).map { code -> orderStatusLabel(code) }
        assertEquals(listOf("Processing", "Approved", "Backordered", "Rejected", "Shipped", "Cancelled"), labels)
    }
    @Test
    fun `an unknown status code reads Unknown Status`() {
        val status = orderStatusLabel(99)
        assertEquals("Unknown Status", status)
    }

    @Test
    fun `zero is not a known status either`() {
        val status = orderStatusLabel(0)
        assertEquals("Unknown Status", status)
    }

}