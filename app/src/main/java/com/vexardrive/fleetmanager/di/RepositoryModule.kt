package com.vexardrive.fleetmanager.di

import com.vexardrive.fleetmanager.data.remote.api.manager.VehicleApi
import com.vexardrive.fleetmanager.data.repository.AuthRepositoryImpl
import com.vexardrive.fleetmanager.data.repository.manager.AssignmentRepositoryImpl
import com.vexardrive.fleetmanager.data.repository.manager.DashboardRepositoryImpl
import com.vexardrive.fleetmanager.data.repository.manager.DriverRepositoryImpl
import com.vexardrive.fleetmanager.data.repository.manager.VehicleRepositoryImpl
import com.vexardrive.fleetmanager.domain.repository.AuthRepository
import com.vexardrive.fleetmanager.domain.repository.manager.AssignmentRepository
import com.vexardrive.fleetmanager.domain.repository.manager.DashboardRepository
import com.vexardrive.fleetmanager.domain.repository.manager.DriverRepository
import com.vexardrive.fleetmanager.domain.repository.manager.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        implementation: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        impl: DashboardRepositoryImpl
    ): DashboardRepository

    @Binds
    @Singleton
    abstract fun provideVehicleRepository(
        impl: VehicleRepositoryImpl
    ): VehicleRepository

    @Binds
    @Singleton
    abstract fun bindDriverRepository(
        repository: DriverRepositoryImpl
    ): DriverRepository

    @Binds
    @Singleton
    abstract fun bindAssignmentRepository(
        impl: AssignmentRepositoryImpl
    ): AssignmentRepository
}