package com.pnc.customercompanion.features.products.data

import com.pnc.customercompanion.features.products.domain.Product
import com.pnc.customercompanion.features.products.domain.ProductItem

object ProductMapper {
    fun mapToDomain(dto: ProductDto): Product {
        return Product(
            id = dto.productId,
            name = dto.name,
            productNumber = dto.productNumber,
            photo = dto.thumbnailPhoto,
            warranty = dto.warranty,
            color = dto.color,
            listPrice = dto.listPrice,
            stockLevel = dto.stockLevel
        )
    }

    fun mapToDomain(dto: ProductItemDto?): ProductItem? {
        if (dto == null) return null
        return ProductItem(
            id = dto.productId,
            name = dto.productName,
            productNumber = dto.productNumber,
            photo = dto.photo,
            color = dto.color,
            listPrice = dto.listPrice,
            stockLevel = dto.stockLevel,
            productLine = dto.productLine,
            productModelId = dto.productModelId,
            productModelName = dto.productModelName,
            manufacturer = dto.manufacturer,
            material = dto.material,
            style = dto.style,
            numberOfSteps = dto.numberOfSteps,
            setupHours = dto.setupHours,
            machineHours = dto.machineHours,
            laborHours = dto.laborHours,
            lotSize = dto.lotSize
        )
    }

    fun mapToDto(domain: Product): ProductDto {
        return ProductDto(
            productId = domain.id,
            name = domain.name,
            productNumber = domain.productNumber,
            thumbnailPhoto = domain.photo,
            warranty = domain.warranty,
            color = domain.color,
            listPrice = domain.listPrice,
            stockLevel = domain.stockLevel
        )
    }

    fun mapToDto(domain: ProductItem): ProductItemDto {
        return ProductItemDto(
            productId = domain.id,
            productName = domain.name,
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