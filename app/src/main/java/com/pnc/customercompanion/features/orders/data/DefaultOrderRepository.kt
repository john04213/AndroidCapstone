package com.pnc.customercompanion.features.orders.data

import com.pnc.customercompanion.core.session.DemoCustomer
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderRepository
import javax.inject.Inject

class DefaultOrderRepository @Inject constructor(
    private val orderApi: OrderApi,
    private val demoCustomer: DemoCustomer
): OrderRepository {
    override suspend fun getOrders(): List<Order> {
        val rows = orderApi.getOrderRows()
        return mapToOrders(rows,demoCustomer.id )
    }
}
