package com.vexardrive.fleetmanager.presentation.manager.vehicle.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleResponse

sealed class VehicleUiState {
    data object Idle : VehicleUiState()
    data object Loading : VehicleUiState()
    data object Empty : VehicleUiState()
    data class Success(val data: VehicleResponse) : VehicleUiState()
    data class Error(val message: String) : VehicleUiState()
}