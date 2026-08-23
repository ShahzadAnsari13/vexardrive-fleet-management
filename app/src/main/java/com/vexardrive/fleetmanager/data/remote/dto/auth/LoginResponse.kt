package com.vexardrive.fleetmanager.data.remote.dto.auth

data class LoginResponse (
    val accessToken: String,
    val refreshToken: String,
    val role :String
)