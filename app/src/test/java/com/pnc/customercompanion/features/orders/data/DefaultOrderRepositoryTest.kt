package com.pnc.customercompanion.features.orders.data

import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.session.DemoCustomer
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException

class DefaultOrderRepositoryTest {
    private class FakeOrderApi(private val rows: List<OrderDto>): OrderApi {
        override suspend fun getOrderRows(): List<OrderDto> {
            return rows
        }
    }

    // an api that always fails with the error it was given
    private class FailingOrderApi(private val error: Throwable): OrderApi {
        override suspend fun getOrderRows(): List<OrderDto> {
            throw error
        }
    }

    // build one api row
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
    fun `only the demo customer orders come back`()= runTest {
        val rows = listOf(
            createRow(11794,75087,34.99),
            createRow(11502,75069,4.99)
        )

        val repository = DefaultOrderRepository(FakeOrderApi(rows), DemoCustomer())

        val orders = repository.getOrders()

        assertEquals(1, orders.size)
        assertEquals(75069, orders[0].orderNumber)
    }

    @Test
    fun `no internet leaves as a LoadException with NO_CONNECTION`() = runTest{
        val repo = DefaultOrderRepository(FailingOrderApi(IOException()), DemoCustomer())

        try{
            repo.getOrders()
            fail("Expected a LoadException")
        } catch (error: LoadException){
            assertEquals(LoadFailure.NO_CONNECTION, error.failure)
        }
    }
}