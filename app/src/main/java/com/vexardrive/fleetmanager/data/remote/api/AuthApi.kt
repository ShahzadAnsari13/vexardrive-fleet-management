package com.vexardrive.fleetmanager.data.remote.api

import com.vexardrive.fleetmanager.data.remote.dto.auth.AuthResponse
import com.vexardrive.fleetmanager.data.remote.dto.auth.LoginRequest
import com.vexardrive.fleetmanager.data.remote.dto.auth.LoginResponse
import com.vexardrive.fleetmanager.data.remote.dto.auth.RefreshTokenApiResponse
import com.vexardrive.fleetmanager.data.remote.dto.auth.RefreshTokenRequestDto
import com.vexardrive.fleetmanager.data.remote.dto.auth.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>


    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequestDto
    ): Response<RefreshTokenApiResponse>

}