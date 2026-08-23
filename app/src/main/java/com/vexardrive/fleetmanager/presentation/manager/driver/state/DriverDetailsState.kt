package com.vexardrive.fleetmanager.presentation.manager.driver.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverDetailsDto

sealed class DriverDetailsState {

    data object Idle : DriverDetailsState()

    data object Loading : DriverDetailsState()

    data class Success(
        val driver: DriverDetailsDto
    ) : DriverDetailsState()

    data class Error(
        val message: String
    ) : DriverDetailsState()
}