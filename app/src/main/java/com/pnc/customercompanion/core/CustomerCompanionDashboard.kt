package com.pnc.customercompanion.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun CustomerCompanionDashboard(
    onViewProducts: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Discover",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag("dashboard-title")

        )
        Button(
            modifier = Modifier.fillMaxWidth()
                .testTag("view-products"),
            onClick = onViewProducts
        ) {
            Text("Browse all products  ")
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Chevron Right"
            )
        }
    }
}