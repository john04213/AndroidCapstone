package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pnc.customercompanion.features.products.domain.ProductDetail
import com.pnc.customercompanion.ui.theme.AndroidCapstoneTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductDetailsContent(
    product: ProductDetail,
) {

    val imageModel: Any? = remember(product.thumbnailPhoto) {
        val raw = product.thumbnailPhoto
        if (raw.isBlank()) null
        else if (raw.startsWith("http")) raw
        else {
            try {
                android.util.Base64.decode(raw, android.util.Base64.DEFAULT)
            } catch (e: Exception) {
                null
            }
        }
    }
    val usdFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        AsyncImage(
            model = imageModel,
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(Modifier.height(16.dp))

        Text(product.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            usdFormat.format(product.price),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(12.dp))

        Spacer(Modifier.height(12.dp))

        Text(
            product.summary,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Stock level",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${product.stockLevel}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Color",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    product.color ?: "no color available",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Product number",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    product.productNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Warranty",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    product.warranty ?: "no warranty available",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)


        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                Icons.Outlined.BookmarkBorder,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Save product")
        }

        Spacer(Modifier.height(16.dp))
    }
}

    @Preview(showBackground = true)
    @Composable
    fun ProductDetailsPreview() {
        val sampleProduct = ProductDetail(
            id = 680,
            name = "Mountain Bike Zero, 901",
            productNumber = "56465138543",
            color = "Blue",
            warranty = "3 years limited",
            price = 1939.98,
            thumbnailPhoto = "",
            summary = "Our lightest and best quality aluminum frame made from the newest alloy; welded and heat-treated for strength.",
            stockLevel = 123
        )

        AndroidCapstoneTheme {
                ProductDetailsContent(product = sampleProduct)
        }
    }

