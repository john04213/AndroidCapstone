package com.pnc.customercompanion.core.error

import kotlinx.serialization.SerializationException
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.format.DateTimeParseException

class LoadFailureMapperTest {
    @Test
    fun `a timeout becomes TIMEOUT`(){
        val failure = toLoadFailure(SocketTimeoutException())
        assertEquals(LoadFailure.TIMEOUT, failure)
    }

    @Test
    fun `no internet becomes NO_CONNECTION`(){
        val failure = toLoadFailure(IOException())
        assertEquals(LoadFailure.NO_CONNECTION, failure)
    }

    @Test
    fun `an HTTP error becomes HTTP_ERROR`(){
        val serverAnswer = HttpException(Response.error<Any>(500, "".toResponseBody()))
        val failure = toLoadFailure(serverAnswer)
        assertEquals(LoadFailure.HTTP_ERROR, failure)
    }

    @Test
    fun `unreadable Json becomes BAD_DATA`(){
        val failure = toLoadFailure(SerializationException())
        assertEquals(LoadFailure.BAD_DATA, failure)
    }

    @Test
    fun `an unreadable date becomes BAD_DATA`() {
        val failure = toLoadFailure(DateTimeParseException("bad date", "abc", 0))
        assertEquals(LoadFailure.BAD_DATA, failure)
    }

    @Test
    fun `an unknown error becomes UNKNOWN`(){
        val failure = toLoadFailure(RuntimeException())
        assertEquals(LoadFailure.UNKNOWN, failure)
    }
}