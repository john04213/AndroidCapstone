package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
val LocalPaneAnimationScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

@Composable
fun Modifier.productImageSharedElement(productId: Int): Modifier {
    val sharedScope = LocalSharedTransitionScope.current ?: return this
    val animatedScope = LocalPaneAnimationScope.current ?: return this

    return with(sharedScope) {
        this@productImageSharedElement.sharedElement(
            sharedContentState = rememberSharedContentState(key = "product-image-$productId"),
            animatedVisibilityScope = animatedScope
        )
    }
}