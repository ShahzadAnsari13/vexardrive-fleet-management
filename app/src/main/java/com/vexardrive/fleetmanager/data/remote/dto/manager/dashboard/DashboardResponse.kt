package com.vexardrive.fleetmanager.data.remote.dto.manager.dashboard

data class DashboardResponse(
    val vehicles: VehicleSummaryDto,
    val trips: ActiveTripDto,
    val todayDistance: TodayDistanceDto,
    val maintenanceDue: MaintenanceDueDto,
    val expiringDocuments: ExpiringDocumentsDto,
    val recentIncidents: List<RecentIncidentDto>
)

data class VehicleSummaryDto(
    val total_vehicles: String,
    val available_vehicles: String,
    val on_trip_vehicles: String,
    val in_maintenance_vehicles: String,
    val inactive_vehicles: String
)

data class ActiveTripDto(
    val active_trips: String
)

data class TodayDistanceDto(
    val today_distance: String
)

data class MaintenanceDueDto(
    val maintenance_due: String
)

data class ExpiringDocumentsDto(
    val insurance: String,
    val registration: String,
    val license: String
)

data class RecentIncidentDto(
    val id: String,
    val vehicle_id: String,
    val registration_number: String,
    val driver_id: String?,
    val issue: String,
    val severity: String,
    val description: String?,
    val timestamp: String,
    val status: String
)