package com.vexardrive.fleetmanager.data.remote.dto.manager.assignment
data class CreateAssignmentRequest(
    val driverId: String,
    val vehicleId: String,
    val startDate: String,
    val endDate: String
)