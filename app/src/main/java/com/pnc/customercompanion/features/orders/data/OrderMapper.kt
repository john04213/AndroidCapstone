package com.pnc.customercompanion.features.orders.data

import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderLines
import java.time.LocalDateTime


// one row is one product line, money becomes exact
private fun toOrderLine(row: OrderDto): OrderLines {
    return OrderLines(
        productName = row.productName,
        orderQty = row.orderQty,
        unitPrice = row.unitPrice.toBigDecimal(),
        lineTotal = row.lineTotal.toBigDecimal()
    )
}

//a missing ship date remains empty
private fun parseShipDate(text: String?): LocalDateTime?{
    return if(text.isNullOrEmpty()) null else LocalDateTime.parse(text)
}

//all the rows of one order become one Order
private fun buildOrder(orderRows:List<OrderDto>): Order {
    val firstRow = orderRows.first()
    return Order(
        orderNumber = firstRow.orderNumber,
        orderDate = LocalDateTime.parse(firstRow.orderDate),
        shipDate = parseShipDate(firstRow.shipDate),
        statusCode = firstRow.status,
        orderLines = orderRows.map { toOrderLine(it) }
    )
}

//keeps one customer's rows, then makes one Order per order number
fun mapToOrders(rows: List<OrderDto>, customerId: Int): List<Order> {
    val customerRows = rows.filter { it.customerId == customerId }
    val rowsByOrderNumber = customerRows.groupBy { it.orderNumber }
    return rowsByOrderNumber.values.map {buildOrder(it)}
}
