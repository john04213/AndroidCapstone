package com.pnc.customercompanion.features.orders.presentation

import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDateTime

class OrdersViewModelTest {

    private class FakeOrderRepository(
      private val orders: List<Order> = emptyList(),
      var failure: LoadFailure? = null
    ): OrderRepository {

        override suspend fun getOrders(): List<Order> {
            val failureNow = failure
            if (failureNow != null) {
                throw LoadException(failureNow, IOException())
            } else {
                return orders
            }
        }
    }

    private fun createOrder(orderNumber: Int): Order {
        return Order(
            orderNumber = orderNumber,
            orderDate = LocalDateTime.parse("2013-06-29T00:00:00"),
            shipDate = null,
            statusCode = 5,
            orderLines = emptyList()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `starts loading then shows the orders`() = runTest {
        val orders = listOf(
            createOrder(75069),
        )

        val viewModel = OrdersViewModel(FakeOrderRepository(orders))
        assertEquals(LoadState.Loading, viewModel.uiState.value)
        advanceUntilIdle()
        assertEquals(LoadState.Success(orders), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `no orders shows Empty`() = runTest {
        val viewModel = OrdersViewModel(FakeOrderRepository())

        advanceUntilIdle()
        assertEquals(LoadState.Empty, viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a failed load shows Failed with its kind`() = runTest {
        val viewModel = OrdersViewModel(FakeOrderRepository(failure = LoadFailure.NO_CONNECTION))

        advanceUntilIdle()
        assertEquals(LoadState.Failed(LoadFailure.NO_CONNECTION), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `retry after a failure loads again and shows the orders`() = runTest {

        val orders = listOf(
            createOrder(75069)
        )

        val repository = FakeOrderRepository(orders, failure = LoadFailure.TIMEOUT)
        val viewModel = OrdersViewModel(repository)
        advanceUntilIdle()


        repository.failure = null
        viewModel.loadOrders()
        assertEquals(LoadState.Loading, viewModel.uiState.value)
        advanceUntilIdle()
        assertEquals(LoadState.Success(orders), viewModel.uiState.value)
    }
}