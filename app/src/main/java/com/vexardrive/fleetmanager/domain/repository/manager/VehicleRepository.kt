package com.vexardrive.fleetmanager.domain.repository.manager

import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.CreateVehicleRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.CreateVehicleResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleStatusRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleStatusResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleDetailResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleResponse

interface VehicleRepository {
    suspend fun getVehicles(): Result<VehicleResponse>
    suspend fun getVehicleById(
        vehicleId: String
    ): Result<VehicleDetailResponse>


    suspend fun updateVehicle(
        vehicleId: String,
        request: UpdateVehicleRequest
    ): Result<UpdateVehicleResponse>

    suspend fun updateVehicleStatus(
        vehicleId: String,
        request: UpdateVehicleStatusRequest
    ): Result<UpdateVehicleStatusResponse>

    suspend fun createVehicle(
        request: CreateVehicleRequest
    ): Result<CreateVehicleResponse>
}