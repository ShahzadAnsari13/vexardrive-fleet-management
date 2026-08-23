package com.vexardrive.fleetmanager.data.remote.dto.manager.driver

data class CreateDriverRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val licenseNumber: String,
    val licenseExpiry: String
)