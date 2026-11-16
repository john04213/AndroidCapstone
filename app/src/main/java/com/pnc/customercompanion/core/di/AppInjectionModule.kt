package com.pnc.customercompanion.core.di

import com.pnc.customercompanion.features.products.domain.DefaultProductRepository
import com.pnc.customercompanion.features.products.domain.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppInjectionModule {
    @Binds
    abstract fun bindProductRepository(
        implementation: DefaultProductRepository
    ): ProductRepository
}