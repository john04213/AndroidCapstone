package com.pnc.customercompanion.features.products.domain

import com.pnc.customercompanion.core.error.LoadFailure
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.format.DateTimeParseException

enum class Availability {
    IN_STOCK,
    LOW_STOCK,
    OUT_OF_STOCK,
    UNKNOWN }

const val LOW_STOCK_THRESHOLD = 200


fun toLoadAvailability(stockLevel: Int?): Availability {
    return when{
        stockLevel == null || stockLevel < 0 -> Availability.UNKNOWN
        stockLevel == 0 -> Availability.OUT_OF_STOCK
        stockLevel <= LOW_STOCK_THRESHOLD -> Availability.LOW_STOCK
        else -> Availability.IN_STOCK
    }
}

fun Product.availability(): Availability = toLoadAvailability(stockLevel)
fun ProductItem.availability(): Availability = toLoadAvailability(stockLevel)