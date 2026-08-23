package com.vexardrive.fleetmanager.domain.repository.manager

import com.vexardrive.fleetmanager.data.remote.dto.manager.dashboard.DashboardResponse

interface DashboardRepository {
    suspend fun getDashboard(): Result<DashboardResponse>
}