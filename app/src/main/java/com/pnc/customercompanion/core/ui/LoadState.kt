package com.pnc.customercompanion.core.ui

import com.pnc.customercompanion.core.error.LoadFailure

// the state of the screen when loading data
sealed interface LoadState<out T>{
    //waiting for the data, nothing to show yet
     data object Loading: LoadState<Nothing>

    //the load worked but there is nothing to show
    data object Empty: LoadState<Nothing>

    // the load worked and there is data to show
    data class Success<T>(val data: T): LoadState<T>

    // load failed, explain the failure
    data class Failed(val failure: LoadFailure): LoadState<Nothing>
}