package com.vexardrive.fleetmanager.data.repository.manager

import com.vexardrive.fleetmanager.data.remote.api.manager.DashboardApi
import com.vexardrive.fleetmanager.data.remote.dto.manager.dashboard.DashboardResponse
import com.vexardrive.fleetmanager.domain.repository.manager.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardApi: DashboardApi
) : DashboardRepository {

    override suspend fun getDashboard(): Result<DashboardResponse> {
        return try {
            println("🔥 DASHBOARD API CALL STARTED")

            val response = dashboardApi.getDashboard()

            if (response.isSuccessful) {

                val body = response.body()

                if (body?.success == true) {
                    Result.success(body.data)
                } else {
                    Result.failure(
                        Exception("Failed to fetch dashboard")
                    )
                }

            } else {
                Result.failure(
                    Exception("Dashboard request failed: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}