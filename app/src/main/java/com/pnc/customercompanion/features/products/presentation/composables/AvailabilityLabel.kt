package com.pnc.customercompanion.features.products.presentation.composables

import com.pnc.customercompanion.features.products.domain.Availability

fun availabilityLabel(availability: Availability): String {
    return when (availability) {
        Availability.IN_STOCK -> "In stock"
        Availability.LOW_STOCK -> "Low stock"
        Availability.OUT_OF_STOCK -> "Out of stock"
        Availability.UNKNOWN -> "Availability unknown"
    }
}
