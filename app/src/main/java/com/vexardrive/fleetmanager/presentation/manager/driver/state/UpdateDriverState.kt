package com.vexardrive.fleetmanager.presentation.manager.driver.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverResponse

sealed class UpdateDriverState {

    data object Idle : UpdateDriverState()

    data object Loading : UpdateDriverState()

    data class Success(
        val response: UpdateDriverResponse
    ) : UpdateDriverState()

    data class Error(
        val message: String
    ) : UpdateDriverState()
}