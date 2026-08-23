package com.vexardrive.fleetmanager.domain.repository.manager

import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverDetailsResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverStatusRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverStatusResponse

interface DriverRepository {
    suspend fun getDrivers(): Result<DriverResponse>
    suspend fun createDriver(
        request: CreateDriverRequest
    ): Result<CreateDriverResponse>

    suspend fun getDriverById(
        driverId: String
    ): Result<DriverDetailsResponse>

    suspend fun updateDriver(
        driverId: String,
        request: UpdateDriverRequest
    ): Result<UpdateDriverResponse>

    suspend fun updateDriverStatus(
        driverId: String,
        request: UpdateDriverStatusRequest
    ): Result<UpdateDriverStatusResponse>
}