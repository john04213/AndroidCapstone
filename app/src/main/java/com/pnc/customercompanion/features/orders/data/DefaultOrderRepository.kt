package com.pnc.customercompanion.features.orders.data

import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.error.toLoadFailure
import com.pnc.customercompanion.core.session.DemoCustomer
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class DefaultOrderRepository @Inject constructor(
    private val orderApi: OrderApi,
    private val demoCustomer: DemoCustomer
): OrderRepository {
    override suspend fun getOrders(): List<Order> {
        try {
            val rows = orderApi.getOrderRows()
            return mapToOrders(rows, demoCustomer.id)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            throw LoadException(toLoadFailure(error), error)
        }
    }
}
