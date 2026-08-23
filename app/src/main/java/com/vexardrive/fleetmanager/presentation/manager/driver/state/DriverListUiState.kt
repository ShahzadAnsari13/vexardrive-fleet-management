package com.vexardrive.fleetmanager.presentation.manager.driver.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverDto

sealed class DriverListUiState {

    data object Idle : DriverListUiState()

    data object Loading : DriverListUiState()

    data class Success(
        val drivers: List<DriverDto>
    ) : DriverListUiState()

    data object Empty : DriverListUiState()

    data class Error(
        val message: String
    ) : DriverListUiState()
}