package com.vexardrive.fleetmanager.data.remote.dto.manager.assignment

data class AssignmentListResponse(
    val success: Boolean,
    val data: List<AssignmentDto>
)

data class AssignmentDto(
    val id: String,
    val driver_name: String,
    val registration_number: String,
    val start_date: String,
    val end_date: String
)