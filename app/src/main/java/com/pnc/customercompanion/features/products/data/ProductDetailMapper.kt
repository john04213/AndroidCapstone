package com.pnc.customercompanion.features.products.data

import com.pnc.customercompanion.features.products.domain.ProductDetail

object ProductDetailMapper {
    fun mapToDomain(dto: ProductDetailDto): ProductDetail {
        return ProductDetail(
            id = dto.productId,
            name = dto.name,
            productNumber = dto.productNumber,
            color = dto.color,
            warranty = dto.warranty,
            price = dto.listPrice,
            thumbnailPhoto = dto.thumbnailPhoto,
            summary = dto.summary,
            stockLevel = dto.stockLevel
        )
    }

    fun mapToDto(domain: ProductDetail): ProductDetailDto {
        return ProductDetailDto(
            productId = domain.id,
            name = domain.name,
            productNumber = domain.productNumber,
            thumbnailPhoto = domain.thumbnailPhoto,
            warranty = domain.warranty,
            color = domain.color,
            listPrice = domain.price,
            summary = domain.summary,
            stockLevel = domain.stockLevel
        )
    }
}