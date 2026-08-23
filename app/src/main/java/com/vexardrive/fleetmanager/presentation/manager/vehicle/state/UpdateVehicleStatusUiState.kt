package com.vexardrive.fleetmanager.presentation.manager.vehicle.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleStatusResponse

sealed class UpdateVehicleStatusUiState {

    data object Idle : UpdateVehicleStatusUiState()

    data object Loading : UpdateVehicleStatusUiState()

    data class Success(
        val data: UpdateVehicleStatusResponse
    ) : UpdateVehicleStatusUiState()

    data class Error(
        val message: String
    ) : UpdateVehicleStatusUiState()
}