package com.pnc.customercompanion.features.orders.presentation

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class OrderShipDateTest {
    @Test
    fun `a ship date is shown as month day and year`() {
        val text = formatShipDate(LocalDateTime.parse("2014-07-06T00:00:00"))
        assertEquals("Jul 6, 2014", text)
    }

    @Test
    fun `a missing ship date reads Not shipped yet`() {
        val text = formatShipDate(null)
        assertEquals("Not shipped yet", text)
    }
}
