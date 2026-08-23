package com.vexardrive.fleetmanager.data.remote.dto.manager.driver

data class DriverResponse(
    val success: Boolean,
    val data: List<DriverDto>
)

data class DriverDto(
    val id: String,
    val name: String,
    val license_number: String,
    val license_expiry: String,
    val status: String
)