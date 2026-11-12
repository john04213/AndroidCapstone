package com.pnc.customercompanion.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass

@Composable
fun CustomerCompanionApp() {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val useTwoPaneLayout = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        useTwoPaneLayout = useTwoPaneLayout
    )
}