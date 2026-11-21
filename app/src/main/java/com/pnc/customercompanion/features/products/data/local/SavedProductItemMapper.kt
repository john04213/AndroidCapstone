package com.pnc.customercompanion.features.products.data.local

import com.pnc.customercompanion.features.products.domain.Product
import com.pnc.customercompanion.features.products.domain.ProductItem

object SavedProductItemMapper {

    // details screen product -> saved row
    fun toSaved(product: ProductItem): SavedProductItem {
        return SavedProductItem(
            id = product.id,
            name = product.name,
            productNumber = product.productNumber,
            photo = product.photo,
            color = product.color,
            listPrice = product.listPrice,
            stockLevel = product.stockLevel
        )
    }

    // saved row -> the Product the list cards show
    fun toDomain(saved: SavedProductItem): Product {
        return Product(
            id = saved.id,
            name = saved.name,
            productNumber = saved.productNumber,
            photo = saved.photo,
            warranty = null,
            color = saved.color,
            listPrice = saved.listPrice,
            stockLevel = saved.stockLevel
        )
    }
}