package com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle

data class UpdateVehicleRequest(
    val registrationNumber: String,
    val vehicleType: String,
    val make: String,
    val model: String,
    val year: Int,
    val fuelType: String,
    val currentMileage: Int,
    val insuranceExpiry: String,
    val registrationExpiry: String
)