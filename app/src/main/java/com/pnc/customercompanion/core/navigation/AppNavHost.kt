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
import com.pnc.customercompanion.features.products.presentation.composables.SavedScreen

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
                },
                onViewSaved = {
                    navController.navigate(SavedRoute)
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
                productId = route.productId,
                onBack = { navController.popBackStack() }
            )
        }

        composable<OrdersRoute> {
            OrdersScreen()
        }

        composable<SavedRoute> {
            SavedScreen(
                onProductSelected = { productId ->
                    navController.navigate(ProductItemRoute(productId))
                },
                onBrowseProducts = {
                    navController.navigate(ProductListRoute)
                }
            )
        }
    }
}