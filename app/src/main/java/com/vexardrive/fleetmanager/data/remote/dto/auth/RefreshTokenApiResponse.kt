package com.vexardrive.fleetmanager.data.remote.dto.auth

data class RefreshTokenApiResponse(
    val success: Boolean,
    val message: String?,
    val data: RefreshTokenResponseDto
)