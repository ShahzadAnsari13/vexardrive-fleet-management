package com.vexardrive.fleetmanager.presentation.auth.state

sealed interface AuthState {

    data object Idle : AuthState

    data object Loading : AuthState
}