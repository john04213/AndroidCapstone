package com.pnc.customercompanion.core.error

// different kind of failure a screen can explain to the customer
enum class LoadFailure {
    NO_CONNECTION,
    TIMEOUT,
    HTTP_ERROR,
    BAD_DATA,
    UNKNOWN
}

// what a repository throws when loading fails
class LoadException(val failure: LoadFailure, cause: Throwable): Exception(cause)