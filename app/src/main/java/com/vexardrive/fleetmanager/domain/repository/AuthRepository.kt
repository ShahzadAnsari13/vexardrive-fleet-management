package com.vexardrive.fleetmanager.domain.repository

import com.vexardrive.fleetmanager.data.remote.dto.auth.AuthResponse
import com.vexardrive.fleetmanager.data.remote.dto.auth.LoginRequest
import com.vexardrive.fleetmanager.data.remote.dto.auth.LoginResponse
import com.vexardrive.fleetmanager.data.remote.dto.auth.RegisterRequest

interface AuthRepository {
    suspend fun register(
        request: RegisterRequest
    ): Result<AuthResponse>
    suspend fun login(
        request: LoginRequest
    ): Result<LoginResponse>
}