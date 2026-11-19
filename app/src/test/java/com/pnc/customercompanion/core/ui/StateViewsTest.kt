package com.pnc.customercompanion.core.ui

import com.pnc.customercompanion.core.error.LoadFailure
import org.junit.Assert.assertEquals
import org.junit.Test

class StateViewsTest {

    @Test
    fun `no connection tells the customer they are offline`() {
        val message = loadFailureMessage(LoadFailure.NO_CONNECTION)
        assertEquals("You're offline. Check your connection and try again", message)
    }

    @Test
    fun `a timeout tells the customer it took too long`() {
        val message = loadFailureMessage(LoadFailure.TIMEOUT)
        assertEquals("This is taking too long. Please try again", message)
    }

    @Test
    fun `every failure kind has its own message`() {
        val messages = LoadFailure.entries.map {failure ->  loadFailureMessage(failure) }

        assertEquals(LoadFailure.entries.size, messages.toSet().size)
    }
}