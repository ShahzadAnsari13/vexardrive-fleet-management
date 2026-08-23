package com.vexardrive.fleetmanager.data.remote.api.manager

import com.vexardrive.fleetmanager.data.remote.dto.manager.dashboard.DashboardApiResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.dashboard.DashboardResponse
import retrofit2.Response
import retrofit2.http.GET

interface DashboardApi {
    @GET("dashboard")
    suspend fun getDashboard(): Response<DashboardApiResponse>
}