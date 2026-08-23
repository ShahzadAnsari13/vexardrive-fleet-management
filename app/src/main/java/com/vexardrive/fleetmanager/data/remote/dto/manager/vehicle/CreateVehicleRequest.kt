package com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle

data class CreateVehicleRequest(
    val registrationNumber: String,
    val vehicleType: String,
    val make: String,
    val model: String,
    val year: Int,
    val fuelType: String,
    val currentMileage: Double,
    val insuranceExpiry: String,
    val registrationExpiry: String
)