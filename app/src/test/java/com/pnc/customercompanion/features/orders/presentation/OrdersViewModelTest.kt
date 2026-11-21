package com.pnc.customercompanion.features.orders.presentation

import com.pnc.customercompanion.core.error.LoadException
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.core.ui.LoadState
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.domain.OrderRepository
import com.pnc.customercompanion.features.orders.domain.OrderSort
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
        var orders: List<Order> = emptyList(),
        var failure: LoadFailure? = null
    ) : OrderRepository {
        // how many times the ViewModel asked for the orders
        var getOrderCalls = 0

        override suspend fun getOrders(): List<Order> {
            getOrderCalls += 1
            val failureNow = failure
            if (failureNow != null) {
                throw LoadException(failureNow, IOException())
            } else {
                return orders
            }
        }
        override suspend fun getOrder(orderNumber: Int): Order? {
            return orders.firstOrNull { order -> order.orderNumber == orderNumber }
        }
    }

    private fun createOrder(
        orderNumber: Int,
        dateName: String = "2013-06-29T00:00:00"
    ): Order {
        return Order(
            orderNumber = orderNumber,
            orderDate = LocalDateTime.parse(dateName),
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `orders are shown newest first`() = runTest {
        val older = createOrder(74977, "2014-05-01T00:00:00")
        val new = createOrder(75069, "2014-06-29T00:00:00")

        val viewModel = OrdersViewModel(FakeOrderRepository(listOf(older, new)))

        advanceUntilIdle()
        assertEquals(LoadState.Success(listOf(new, older)), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `changing the sort reorders the list without loading again`() = runTest {
        val older = createOrder(74977, "2014-05-01T00:00:00")
        val new = createOrder(75069, "2014-06-29T00:00:00")
        val repository = FakeOrderRepository(listOf(older, new))
        val viewModel = OrdersViewModel(repository)
        advanceUntilIdle()

        viewModel.changeSort(OrderSort.OLDEST_FIRST)
        assertEquals(LoadState.Success(listOf(older, new)), viewModel.uiState.value)
        assertEquals(OrderSort.OLDEST_FIRST, viewModel.sort.value)
        assertEquals(1, repository.getOrderCalls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refresh keeps the list on screen while it loads`() = runTest {
        val olderOrders = createOrder(74977)
        val newOrders = createOrder(75069)
        val repository = FakeOrderRepository(listOf(olderOrders))
        val viewModel = OrdersViewModel(repository)
        advanceUntilIdle()
        //act
        repository.orders = listOf(newOrders)
        viewModel.refresh()
        //assert
        assertEquals(LoadState.Success(listOf(olderOrders)), viewModel.uiState.value)
        assertEquals(true, viewModel.isRefreshing.value)
        //act
        advanceUntilIdle()
        //assert
        assertEquals(LoadState.Success(listOf(newOrders)), viewModel.uiState.value)
        assertEquals(false, viewModel.isRefreshing.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a failed refresh keeps the list and reports the failure`() = runTest {
        val orders = createOrder(75069)
        val repository = FakeOrderRepository(listOf(orders))
        val viewModel = OrdersViewModel(repository)
        advanceUntilIdle()

        repository.failure = LoadFailure.TIMEOUT
        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(LoadState.Success(listOf(orders)), viewModel.uiState.value)
        assertEquals(LoadFailure.TIMEOUT, viewModel.refreshFailure.value,)
        assertEquals(false, viewModel.isRefreshing.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a second refresh while one is running is ignored`() = runTest {
        val repository = FakeOrderRepository(listOf(createOrder(75069)))
        val viewModel = OrdersViewModel(repository)
        advanceUntilIdle()

        viewModel.refresh()
        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(2, repository.getOrderCalls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `the chosen sort stays after a refresh`() = runTest {
        val older = createOrder(74977, "2014-05-01T00:00:00")
        val new = createOrder(75069, "2014-06-29T00:00:00")
        val viewModel = OrdersViewModel(FakeOrderRepository(listOf(new, older)))
        advanceUntilIdle()

        viewModel.changeSort(OrderSort.OLDEST_FIRST)
        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(LoadState.Success(listOf(older, new)), viewModel.uiState.value)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `typing part of an order number shows only the matching orders`() = runTest {
        val match = createOrder(75069)
        val other = createOrder(74977)
        val repository = FakeOrderRepository(listOf(match, other))
        val viewModel = OrdersViewModel(repository)
        advanceUntilIdle()

        viewModel.changeSearch("750")

        assertEquals(LoadState.Success(listOf(match)), viewModel.uiState.value)
        assertEquals("750", viewModel.searchQuery.value)
        assertEquals(1, repository.getOrderCalls)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `clearing the search shows every order again`() = runTest {
        val older = createOrder(74977, "2014-05-01T00:00:00")
        val new = createOrder(75069, "2014-06-29T00:00:00")
        val repository = FakeOrderRepository(listOf(older, new))
        val viewModel = OrdersViewModel(repository)
        advanceUntilIdle()

        viewModel.changeSearch("750")
        viewModel.changeSearch("")

        assertEquals(LoadState.Success(listOf(new, older)), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `a search with no match shows an empty list and keeps the screen`() = runTest {
        val viewModel = OrdersViewModel(FakeOrderRepository(listOf(createOrder(75069))))
        advanceUntilIdle()

        viewModel.changeSearch("1234")

        assertEquals(LoadState.Success(emptyList<Order>()), viewModel.uiState.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `the search stays after a refresh`() = runTest {
        val match = createOrder(75069)
        val other = createOrder(74977)
        val viewModel = OrdersViewModel(FakeOrderRepository(listOf(match, other)))
        advanceUntilIdle()

        viewModel.changeSearch("750")
        viewModel.refresh()
        advanceUntilIdle()

        assertEquals(LoadState.Success(listOf(match)), viewModel.uiState.value)
    }

    @Test
    fun `the screen gets the demo customer's name`() {
        val viewModel = OrdersViewModel(FakeOrderRepository())
        assertEquals("Jared Peterson", viewModel.customerName)
    }
}