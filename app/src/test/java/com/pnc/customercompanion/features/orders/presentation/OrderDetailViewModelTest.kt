package com.pnc.customercompanion.features.orders.presentation

import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderLines
import com.pnc.customercompanion.features.orders.domain.OrderRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.math.BigDecimal
import java.time.LocalDateTime

class OrderDetailViewModelTest {

    private class FakeOrderRepository(
        var orders: List<Order> = emptyList(),
        var failure: LoadFailure? = null
    ) : OrderRepository {
        // how many times the ViewModel asked for one order
        var getOrderCalls = 0

        // an order number in here waits until its gate is completed, so a test can keep a load running
        val gates = mutableMapOf<Int, CompletableDeferred<Unit>>()

        override suspend fun getOrders(): List<Order> {
            return orders
        }

        override suspend fun getOrder(orderNumber: Int): Order? {
            getOrderCalls += 1
            gates[orderNumber]?.await()
            val failureNow = failure
            if (failureNow != null) {
                throw LoadException(failureNow, IOException())
            } else {
                return orders.firstOrNull { order -> order.orderNumber == orderNumber }
            }
        }
    }

    private fun createOrder(orderNumber: Int): Order {
        return Order(
            orderNumber = orderNumber,
            orderDate = LocalDateTime.parse("2014-06-29T00:00:00"),
            shipDate = null,
            statusCode = 1,
            orderLines = listOf(
                OrderLines(
                    productName = "Touring Tire Tube",
                    orderQty = 1,
                    unitPrice = BigDecimal("4.99"),
                    lineTotal = BigDecimal("4.99")
                )
            )
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
    fun `starts loading then shows the order`() = runTest {
        val order = createOrder(75069)
        val viewModel = OrderDetailViewModel(FakeOrderRepository(listOf(order)))
        assertEquals(LoadState.Loading, viewModel.uiState.value)

        viewModel.loadOrder(75069)
        advanceUntilIdle()

        assertEquals(LoadState.Success(order), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `an order that is not there shows Failed as not found`() = runTest {
        val viewModel = OrderDetailViewModel(FakeOrderRepository(listOf(createOrder(75069))))

        viewModel.loadOrder(11111)
        advanceUntilIdle()

        assertEquals(LoadState.Failed(LoadFailure.NOT_FOUND), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a failed load shows Failed with its kind`() = runTest {
        val viewModel = OrderDetailViewModel(FakeOrderRepository(failure = LoadFailure.NO_CONNECTION))

        viewModel.loadOrder(75069)
        advanceUntilIdle()

        assertEquals(LoadState.Failed(LoadFailure.NO_CONNECTION), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `retry after a failure loads again and shows the order`() = runTest {
        val order = createOrder(75069)
        val repository = FakeOrderRepository(listOf(order), failure = LoadFailure.TIMEOUT)
        val viewModel = OrderDetailViewModel(repository)
        viewModel.loadOrder(75069)
        advanceUntilIdle()

        repository.failure = null
        viewModel.retry()
        assertEquals(LoadState.Loading, viewModel.uiState.value)
        advanceUntilIdle()

        assertEquals(LoadState.Success(order), viewModel.uiState.value)
        assertEquals(2, repository.getOrderCalls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `asking again for the order that is showing does not load again`() = runTest {
        val repository = FakeOrderRepository(listOf(createOrder(75069)))
        val viewModel = OrderDetailViewModel(repository)
        viewModel.loadOrder(75069)
        advanceUntilIdle()

        viewModel.loadOrder(75069)
        advanceUntilIdle()

        assertEquals(1, repository.getOrderCalls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `asking for a different order loads that order`() = runTest {
        val first = createOrder(75069)
        val second = createOrder(74977)
        val viewModel = OrderDetailViewModel(FakeOrderRepository(listOf(first, second)))
        viewModel.loadOrder(75069)
        advanceUntilIdle()

        viewModel.loadOrder(74977)
        advanceUntilIdle()

        assertEquals(LoadState.Success(second), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `asking again for an order that is still loading does not load again`() = runTest {
        val repository = FakeOrderRepository(listOf(createOrder(75069)))
        val gate = CompletableDeferred<Unit>()
        repository.gates[75069] = gate
        val viewModel = OrderDetailViewModel(repository)
        viewModel.loadOrder(75069)
        runCurrent()

        viewModel.loadOrder(75069)
        runCurrent()
        gate.complete(Unit)
        advanceUntilIdle()

        assertEquals(1, repository.getOrderCalls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a slow load of an order the customer left does not replace the order shown`() = runTest {
        val first = createOrder(75069)
        val second = createOrder(74977)
        val repository = FakeOrderRepository(listOf(first, second))
        val firstGate = CompletableDeferred<Unit>()
        val secondGate = CompletableDeferred<Unit>()
        repository.gates[75069] = firstGate
        repository.gates[74977] = secondGate
        val viewModel = OrderDetailViewModel(repository)
        viewModel.loadOrder(75069)
        runCurrent()
        viewModel.loadOrder(74977)
        runCurrent()

        // the second order answers first, then the first order's old answer arrives late
        secondGate.complete(Unit)
        advanceUntilIdle()
        firstGate.complete(Unit)
        advanceUntilIdle()

        assertEquals(LoadState.Success(second), viewModel.uiState.value)
    }
}
