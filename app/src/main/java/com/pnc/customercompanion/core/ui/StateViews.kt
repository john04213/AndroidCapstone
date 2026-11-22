package com.pnc.customercompanion.core.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.pnc.customercompanion.core.error.LoadFailure
import com.pnc.customercompanion.ui.theme.LocalSpacing

// turns a failure kinds into words a customer can understand
fun loadFailureMessage(failure: LoadFailure): String {
    return when (failure) {
        LoadFailure.NO_CONNECTION -> "You're offline. Check your connection and try again"
        LoadFailure.TIMEOUT -> "This is taking too long. Please try again"
        LoadFailure.HTTP_ERROR -> "The server had a problem. Please try again later"
        LoadFailure.BAD_DATA -> "We received information we couldn't read. Please try again later"
        LoadFailure.UNKNOWN -> "Something went wrong. Please try again later"
        LoadFailure.NOT_FOUND -> "We couldn't find what you were looking for. Please try again."
    }
}

// shown while the first load is running
@Composable
fun LoadingView(){
    Column(
        modifier = Modifier.fillMaxSize().padding(LocalSpacing.current.extraLarge),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularProgressIndicator()
        Text(text = "Loading...")
    }
}

// shown when the load worked but there is nothing on the list
@Composable
fun EmptyView(message: String){
    Column(
        modifier = Modifier.fillMaxSize().padding(LocalSpacing.current.extraLarge),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = message)
    }
}

// shown when the load failed says what went wrong
@Composable
fun FailedView(failure: LoadFailure, onRetry: () -> Unit, onBack: (() -> Unit)? = null) {
    Column(
        modifier = Modifier.fillMaxSize().padding(LocalSpacing.current.extraLarge),
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.medium, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = loadFailureMessage(failure), textAlign = TextAlign.Center)
        if (failure == LoadFailure.NOT_FOUND) {
            // retrying won't help, so offer a way out instead
            if (onBack != null) {
                Button(onClick = onBack) { Text(text = "Go back") }
            }
        } else {
            Button(onClick = onRetry) { Text(text = "Retry") }
        }
    }
}


