package com.vexardrive.fleetmanager.presentation.manager.driver.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverResponse

sealed class CreateDriverState {

    data object Idle : CreateDriverState()

    data object Loading : CreateDriverState()

    data class Success(
        val response: CreateDriverResponse
    ) : CreateDriverState()

    data class Error(
        val message: String
    ) : CreateDriverState()
}