package com.pnc.customercompanion.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pnc.customercompanion.core.CustomerCompanionDashboard
import com.pnc.customercompanion.features.orders.presentation.OrdersScreen
import com.pnc.customercompanion.features.products.presentation.composables.ProductDetails
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
                },
                onViewProductItem = { productId ->
                    navController.navigate(ProductItemRoute(productId))
                },
                onViewOrders = {
                    navController.navigate(OrdersRoute)
                }
            )
        }

        composable<ProductListRoute> {
            ProductDirectoryRoute(
                useTwoPaneLayout = useTwoPaneLayout,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<ProductItemRoute> { backStackEntry ->
            val route: ProductItemRoute = backStackEntry.toRoute()
            ProductDetails(
                productId = route.productId
            )
        }

        composable<OrdersRoute> {
            OrdersScreen()
        }
    }
}