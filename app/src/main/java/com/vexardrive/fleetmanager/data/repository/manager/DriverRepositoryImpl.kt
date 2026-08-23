package com.vexardrive.fleetmanager.data.repository.manager

import com.vexardrive.fleetmanager.data.remote.api.manager.DriverApi
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverDetailsResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverStatusRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverStatusResponse
import com.vexardrive.fleetmanager.domain.repository.manager.DriverRepository
import javax.inject.Inject

class DriverRepositoryImpl @Inject constructor(
    private val api: DriverApi
) : DriverRepository {

    override suspend fun getDrivers(): Result<DriverResponse> {
        return try {
            val response = api.getDrivers()

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

    override suspend fun createDriver(
        request: CreateDriverRequest
    ): Result<CreateDriverResponse> {

        return try {
            val response = api.createDriver(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response")
                )
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to create driver"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDriverById(
        driverId: String
    ): Result<DriverDetailsResponse> {

        return try {
            val response = api.getDriverById(driverId)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response")
                )
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to fetch driver details"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDriver(
        driverId: String,
        request: UpdateDriverRequest
    ): Result<UpdateDriverResponse> {

        return try {
            val response = api.updateDriver(driverId, request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response")
                )
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to update driver"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDriverStatus(
        driverId: String,
        request: UpdateDriverStatusRequest
    ): Result<UpdateDriverStatusResponse> {

        return try {
            val response = api.updateDriverStatus(
                driverId,
                request
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response")
                )
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to update driver status"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}