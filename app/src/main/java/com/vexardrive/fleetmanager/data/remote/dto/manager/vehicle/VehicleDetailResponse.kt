package com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle

data class VehicleDetailResponse(
    val success: Boolean,
    val data: VehicleDetailDto
)
data class VehicleDetailDto(
    val id: String,
    val registration_number: String,
    val vehicle_type: String,
    val make: String,
    val model: String,
    val year: Int,
    val fuel_type: String,
    val current_mileage: Int,
    val status: String,
    val insurance_expiry: String,
    val registration_expiry: String,
    val created_at: String,
    val updated_at: String
)