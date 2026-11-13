package com.pnc.customercompanion.features.products.data

import com.pnc.customercompanion.features.products.domain.Product
import kotlin.String

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
}