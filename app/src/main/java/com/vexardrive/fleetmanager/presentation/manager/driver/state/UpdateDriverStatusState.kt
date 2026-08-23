package com.vexardrive.fleetmanager.presentation.manager.driver.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverStatusResponse

sealed class UpdateDriverStatusState {

    data object Idle : UpdateDriverStatusState()

    data object Loading : UpdateDriverStatusState()

    data class Success(
        val response: UpdateDriverStatusResponse
    ) : UpdateDriverStatusState()

    data class Error(
        val message: String
    ) : UpdateDriverStatusState()
}