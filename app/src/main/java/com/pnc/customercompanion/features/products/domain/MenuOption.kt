package com.pnc.customercompanion.features.products.domain

data class MenuOption(
    val text: String,
    val action: () -> Unit
)