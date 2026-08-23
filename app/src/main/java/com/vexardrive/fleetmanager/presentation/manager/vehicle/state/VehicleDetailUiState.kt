package com.vexardrive.fleetmanager.presentation.manager.vehicle.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleDetailResponse

sealed class VehicleDetailUiState {

    data object Idle : VehicleDetailUiState()

    data object Loading : VehicleDetailUiState()

    data class Success(
        val data: VehicleDetailResponse
    ) : VehicleDetailUiState()

    data class Error(
        val message: String
    ) : VehicleDetailUiState()
}