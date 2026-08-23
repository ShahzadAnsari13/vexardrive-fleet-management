package com.vexardrive.fleetmanager.presentation.auth.event

import com.vexardrive.fleetmanager.data.remote.dto.auth.AuthResponse
import com.vexardrive.fleetmanager.data.remote.dto.auth.LoginResponse

sealed interface AuthEvent {

    data class RegisterSuccess(
        val response: AuthResponse
    ) : AuthEvent

    data class LoginSuccess(
        val response: LoginResponse
    ) : AuthEvent

    data class Error(
        val message: String
    ) : AuthEvent
}