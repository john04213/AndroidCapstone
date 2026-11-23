package com.pnc.customercompanion.features.orders.presentation

import java.time.LocalDateTime

// the ship date in customer words; an order with no ship date has not shipped yet
internal fun formatShipDate(shipDate: LocalDateTime?): String {
    return if (shipDate == null) "Not shipped yet" else formatOrderDate(shipDate)
}
