package com.vexardrive.fleetmanager.data.remote.dto.manager.driver

data class UpdateDriverRequest(
    val name: String,
    val email: String,
    val phone: String,
    val licenseNumber: String,
    val licenseExpiry: String
)