package com.pnc.customercompanion.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pnc.customercompanion.core.CustomerCompanionDashboard
import com.pnc.customercompanion.features.products.presentation.composables.ProductDirectoryRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    useTwoPaneLayout: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = DashboardRoute
    ) {
        composable<DashboardRoute> {
            CustomerCompanionDashboard(
                onViewProducts = {
                    navController.navigate(ProductListRoute)
                }
            )
        }

        composable<ProductListRoute> {
            ProductDirectoryRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}