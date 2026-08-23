package com.vexardrive.fleetmanager.data.repository.manager

import com.vexardrive.fleetmanager.data.remote.api.manager.VehicleApi
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.CreateVehicleRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.CreateVehicleResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleStatusRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleStatusResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleDetailResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleResponse
import com.vexardrive.fleetmanager.domain.repository.manager.VehicleRepository
import javax.inject.Inject

class VehicleRepositoryImpl @Inject constructor(
    private val vehicleApi: VehicleApi
) : VehicleRepository {

    override suspend fun getVehicles(): Result<VehicleResponse> {
        return try {
            val response = vehicleApi.getVehicles()

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVehicleById(
        vehicleId: String
    ): Result<VehicleDetailResponse> {

        return try {
            val response = vehicleApi.getVehicleById(vehicleId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(response.message())
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateVehicle(
        vehicleId: String,
        request: UpdateVehicleRequest
    ): Result<UpdateVehicleResponse> {

        return try {
            val response = vehicleApi.updateVehicle(
                vehicleId,
                request
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(response.message())
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateVehicleStatus(
        vehicleId: String,
        request: UpdateVehicleStatusRequest
    ): Result<UpdateVehicleStatusResponse> {

        return try {
            val response = vehicleApi.updateVehicleStatus(
                vehicleId,
                request
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(response.message())
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createVehicle(
        request: CreateVehicleRequest
    ): Result<CreateVehicleResponse> {

        return try {
            val response = vehicleApi.createVehicle(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(response.message())
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}