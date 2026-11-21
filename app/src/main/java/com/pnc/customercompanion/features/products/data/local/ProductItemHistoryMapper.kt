package com.pnc.customercompanion.features.products.data.local

import com.pnc.customercompanion.features.products.data.ProductItemDto
import com.pnc.customercompanion.features.products.domain.ProductItem

object ProductItemHistoryMapper {
    fun toDomain(history: ProductItemHistory): ProductItem {
        return ProductItem(
            id = history.id,
            name = history.name,
            productNumber = history.productNumber,
            photo = history.photo,
            color = history.color,
            listPrice = history.listPrice,
            stockLevel = history.stockLevel,
            productLine = history.productLine,
            productModelId = history.productModelId,
            productModelName = history.productModelName,
            manufacturer = history.manufacturer,
            material = history.material,
            style = history.style,
            numberOfSteps = history.numberOfSteps,
            setupHours = history.setupHours,
            machineHours = history.machineHours,
            laborHours = history.laborHours,
            lotSize = history.lotSize
        )
    }

    fun toHistory(domain: ProductItemDto): ProductItemHistory {
        return ProductItemHistory(
            id = domain.productId,
            name = domain.productName,
            productNumber = domain.productNumber,
            photo = domain.photo,
            color = domain.color,
            listPrice = domain.listPrice,
            stockLevel = domain.stockLevel,
            productLine = domain.productLine,
            productModelId = domain.productModelId,
            productModelName = domain.productModelName,
            manufacturer = domain.manufacturer,
            material = domain.material,
            style = domain.style,
            numberOfSteps = domain.numberOfSteps,
            setupHours = domain.setupHours,
            machineHours = domain.machineHours,
            laborHours = domain.laborHours,
            lotSize = domain.lotSize
        )
    }
}