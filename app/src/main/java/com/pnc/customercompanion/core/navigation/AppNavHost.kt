package com.pnc.customercompanion.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pnc.customercompanion.core.CustomerCompanionDashboard
import com.pnc.customercompanion.features.products.presentation.ProductList

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
            ProductList(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}