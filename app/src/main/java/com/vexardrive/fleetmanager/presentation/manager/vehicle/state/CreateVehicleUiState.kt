package com.vexardrive.fleetmanager.presentation.manager.vehicle.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.CreateVehicleResponse

sealed class CreateVehicleUiState {

    data object Idle : CreateVehicleUiState()

    data object Loading : CreateVehicleUiState()

    data class Success(
        val data: CreateVehicleResponse
    ) : CreateVehicleUiState()

    data class Error(
        val message: String
    ) : CreateVehicleUiState()
}