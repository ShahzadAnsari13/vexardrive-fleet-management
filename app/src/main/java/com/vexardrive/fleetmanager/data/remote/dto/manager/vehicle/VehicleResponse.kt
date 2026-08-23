package com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle

data class VehicleResponse(
    val success: Boolean,
    val data: List<VehicleDto>
)

data class VehicleDto(
    val id: String,
    val registration_number: String,
    val vehicle_type: String,
    val make: String,
    val model: String,
    val status: String
)