package com.vexardrive.fleetmanager.data.remote.api.manager

import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverDetailsResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverStatusRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverStatusResponse
import retrofit2.Response
import retrofit2.http.Body

import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DriverApi {
    @GET("drivers")
    suspend fun getDrivers(): Response<DriverResponse>

    @POST("drivers")
    suspend fun createDriver(
        @Body request: CreateDriverRequest
    ): Response<CreateDriverResponse>

    @GET("drivers/{id}")
    suspend fun getDriverById(
        @Path("id") driverId: String
    ): Response<DriverDetailsResponse>

    @PUT("drivers/{id}")
    suspend fun updateDriver(
        @Path("id") driverId: String,
        @Body request: UpdateDriverRequest
    ): Response<UpdateDriverResponse>

    @PATCH("drivers/{id}/status")
    suspend fun updateDriverStatus(
        @Path("id") driverId: String,
        @Body request: UpdateDriverStatusRequest
    ): Response<UpdateDriverStatusResponse>
}