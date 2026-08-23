package com.vexardrive.fleetmanager.presentation.manager.assignment.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.CreateAssignmentResponse

sealed class CreateAssignmentState {

    data object Idle : CreateAssignmentState()

    data object Loading : CreateAssignmentState()

    data class Success(
        val response: CreateAssignmentResponse
    ) : CreateAssignmentState()

    data class Error(
        val message: String
    ) : CreateAssignmentState()
}