package com.pnc.customercompanion.feature.orders.presentation

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import com.pnc.customercompanion.features.orders.domain.Order
import com.pnc.customercompanion.features.orders.presentation.OrderRow
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class OrdersScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun an_order_row_is_read_as_one_item() {
        val order = Order(
            orderNumber = 75069,
            orderDate = LocalDateTime.parse("2014-06-29T00:00:00"),
            shipDate = null,
            statusCode = 5,
            orderLines = emptyList()
        )

        composeTestRule.setContent { OrderRow(order = order, onClick = {}) }
        composeTestRule.onAllNodes(hasText("Order # 75069") and hasText("Date: Jun 29, 2014")).assertCountEquals(1)
    }
}