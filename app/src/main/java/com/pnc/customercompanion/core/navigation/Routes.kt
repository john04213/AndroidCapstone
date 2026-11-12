package com.pnc.customercompanion.core.navigation

import kotlinx.serialization.Serializable

sealed interface CustomerCompanionRoute

@Serializable
data object DashboardRoute: CustomerCompanionRoute

@Serializable
data object ProductListRoute: CustomerCompanionRoute