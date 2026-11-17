package com.pnc.customercompanion.features.products.domain

enum class SortOrder(val label: String) {
    NAME_ASC("Name"),
    PRICE_ASC("Price: low-high"),
    PRICE_DESC("Price: high-low")
}
