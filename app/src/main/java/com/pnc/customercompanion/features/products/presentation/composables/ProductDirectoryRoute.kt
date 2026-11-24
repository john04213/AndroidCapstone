package com.pnc.customercompanion.features.products.presentation.composables

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pnc.customercompanion.features.products.presentation.viewmodels.ProductDirectoryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ProductDirectoryRoute(
    onBack: () -> Unit = {},
    useTwoPaneLayout: Boolean = false,
    viewModel: ProductDirectoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.loadProducts()
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()
    val scope = rememberCoroutineScope()

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavigableListDetailPaneScaffold(
                navigator = navigator,
                listPane = {
                    AnimatedPane {
                        CompositionLocalProvider(LocalPaneAnimationScope provides this) {
                            ProductDirectory(
                                state = uiState,
                                useTwoPaneLayout = false,
                                onSortOrderChange = viewModel::updateSortOrderChange,
                                onBack = onBack,
                                onProductSelected = { productId ->
                                    scope.launch {
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            productId
                                        )
                                    }
                                },
                                onRefresh = viewModel::loadProducts,
                                onSearchQueryChange = viewModel::updateSearchQuery
                            )
                        }
                    }
                },
                detailPane = {
                    AnimatedPane {
                        CompositionLocalProvider(LocalPaneAnimationScope provides this) {
                            val productId = navigator.currentDestination?.contentKey
                            if (productId != null) {

                                ProductDetails(
                                    productId = productId,
                                    onBack = if (useTwoPaneLayout) null else {
                                        { scope.launch { if (navigator.canNavigateBack()) navigator.navigateBack() } }
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

