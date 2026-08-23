package com.vexardrive.fleetmanager.presentation.manager.vehicle.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleResponse

sealed class UpdateVehicleUiState {

    data object Idle : UpdateVehicleUiState()

    data object Loading : UpdateVehicleUiState()

    data class Success(
        val data: UpdateVehicleResponse
    ) : UpdateVehicleUiState()

    data class Error(
        val message: String
    ) : UpdateVehicleUiState()
}