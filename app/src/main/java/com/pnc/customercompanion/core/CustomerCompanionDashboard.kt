package com.pnc.customercompanion.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.customercompanion.core.ui.base64ToBitmap
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CustomerCompanionDashboard(
    onViewProducts: () -> Unit,
    onViewProductItem: (Int) -> Unit,
    onViewOrders: () -> Unit,
    onViewSaved: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val items by viewModel.productItems.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
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

        Button(
            modifier = Modifier.fillMaxWidth()
                .testTag("view-saved"),
            onClick = onViewSaved
        ) {
            Text("My saved products")
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Chevron Right"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recently viewed",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = {
                    viewModel.onClearHistoryClicked()
                }
            ) {
                Text("Clear history")
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = items,
                key = { it.id }
            ) { item ->
                Card(
                    onClick = {
                        onViewProductItem(item.id)
                    },
                    modifier = Modifier.size(
                        width = 150.dp,
                        height = 150.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val imageBitmap = remember(item.photo) {
                            base64ToBitmap(item.photo)?.asImageBitmap()
                        }
                        imageBitmap?.let { bitmap ->
                            Image(
                                bitmap = bitmap,
                                contentDescription = "${item.name} image",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp)
                            )
                        }
                        Text(
                            text = item.name,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(
                            text = NumberFormat.getCurrencyInstance(Locale.US).format(item.listPrice),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth()
                .testTag("view-orders"),
            onClick = onViewOrders
        ) {
            Text("View my orders  ")
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
    }
}