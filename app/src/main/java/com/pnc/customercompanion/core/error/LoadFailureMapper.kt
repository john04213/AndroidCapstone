package com.pnc.customercompanion.core.error

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.format.DateTimeParseException

// turns a library error into one of the app's failure kinds
fun toLoadFailure(error: Throwable): LoadFailure {
    // give back depending on the error type
    return when(error){
        is SocketTimeoutException -> LoadFailure.TIMEOUT
        is IOException -> LoadFailure.NO_CONNECTION
        is SerializationException -> LoadFailure.BAD_DATA
        is DateTimeParseException -> LoadFailure.BAD_DATA
        is HttpException -> if (error.code() == 404) LoadFailure.NOT_FOUND else LoadFailure.HTTP_ERROR
        else -> LoadFailure.UNKNOWN
    }
}