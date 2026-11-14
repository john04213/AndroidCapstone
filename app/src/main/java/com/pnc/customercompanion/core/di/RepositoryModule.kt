package com.pnc.customercompanion.core.di


import com.pnc.customercompanion.features.orders.data.DefaultOrderRepository
import com.pnc.customercompanion.features.orders.domain.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// tells Hilt which class to hand out for a repository
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // asked for OrderRepository, gives out DefaultOrderRepository
    @Binds
    abstract fun bindOrderRepository(defaultOrderRepository: DefaultOrderRepository): OrderRepository
}