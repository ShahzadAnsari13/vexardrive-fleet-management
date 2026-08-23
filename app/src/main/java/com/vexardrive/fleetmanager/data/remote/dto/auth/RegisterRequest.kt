package com.vexardrive.fleetmanager.data.remote.dto.auth

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val confirmPassword: String,
    val role: String = "FLEET_MANAGER"
)