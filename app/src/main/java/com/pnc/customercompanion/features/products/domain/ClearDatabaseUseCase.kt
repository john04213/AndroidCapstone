package com.pnc.customercompanion.features.products.domain

import javax.inject.Inject

class ClearDatabaseUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke() {
        repository.clearDatabase()
    }
}