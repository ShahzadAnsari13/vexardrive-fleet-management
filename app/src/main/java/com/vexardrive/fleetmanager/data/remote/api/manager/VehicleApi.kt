package com.vexardrive.fleetmanager.data.remote.api.manager

import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.CreateVehicleRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.CreateVehicleResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleStatusRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleStatusResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleDetailResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleResponse
import retrofit2.Response

import retrofit2.http.GET

import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface VehicleApi {
    @GET("vehicles")
    suspend fun getVehicles(): Response<VehicleResponse>

    @GET("vehicles/{id}")
    suspend fun getVehicleById(
        @Path("id") vehicleId: String
    ): Response<VehicleDetailResponse>


    @PUT("vehicles/{id}")
    suspend fun updateVehicle(
        @Path("id") vehicleId: String,
        @Body request: UpdateVehicleRequest
    ): Response<UpdateVehicleResponse>

    @PATCH("vehicles/{id}/status")
    suspend fun updateVehicleStatus(
        @Path("id") vehicleId: String,
        @Body request: UpdateVehicleStatusRequest
    ): Response<UpdateVehicleStatusResponse>

    @POST("vehicles")
    suspend fun createVehicle(
        @Body request: CreateVehicleRequest
    ): Response<CreateVehicleResponse>
}