package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pnc.customercompanion.features.products.domain.Availability


@Composable
fun AvailabilityBadge(availability: Availability) {
        val (icon, color) = when (availability) {
            Availability.IN_STOCK -> Icons.Filled.CheckCircle to MaterialTheme.colorScheme.primaryContainer
            Availability.LOW_STOCK -> Icons.Filled.Warning to MaterialTheme.colorScheme.tertiaryContainer
            Availability.OUT_OF_STOCK -> Icons.Filled.Block to MaterialTheme.colorScheme.errorContainer
            Availability.UNKNOWN -> Icons.Filled.Info to MaterialTheme.colorScheme.surfaceVariant
        }

        Surface(
            shape = RoundedCornerShape(20),
            color = color
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(availabilityLabel(availability), style = MaterialTheme.typography.labelMedium)
            }
        }
    }