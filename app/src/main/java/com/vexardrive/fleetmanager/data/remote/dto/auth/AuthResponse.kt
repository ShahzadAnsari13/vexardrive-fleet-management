package com.vexardrive.fleetmanager.data.remote.dto.auth

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)