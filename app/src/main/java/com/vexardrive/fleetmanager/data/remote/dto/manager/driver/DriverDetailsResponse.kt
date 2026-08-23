package com.vexardrive.fleetmanager.data.remote.dto.manager.driver

data class DriverDetailsResponse(
    val success: Boolean,
    val data: DriverDetailsDto
)

data class DriverDetailsDto(
    val id: String,
    val user_id: String,
    val name: String,
    val email: String,
    val phone: String,
    val license_number: String,
    val license_expiry: String,
    val status: String,
    val created_at: String,
    val updated_at: String
)