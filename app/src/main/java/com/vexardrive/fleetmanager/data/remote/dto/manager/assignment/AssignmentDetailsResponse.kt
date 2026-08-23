package com.vexardrive.fleetmanager.data.remote.dto.manager.assignment

data class AssignmentDetailsResponse(
    val success: Boolean,
    val data: AssignmentDetailsDto
)
data class AssignmentDetailsDto(
    val id: String,
    val driver_name: String,
    val registration_number: String,
    val vehicle_type: String,
    val start_date: String,
    val end_date: String
)