package com.vexardrive.fleetmanager.presentation.manager.assignment.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.DeleteAssignmentResponse

sealed class DeleteAssignmentState {

    data object Idle : DeleteAssignmentState()

    data object Loading : DeleteAssignmentState()

    data class Success(
        val response: DeleteAssignmentResponse
    ) : DeleteAssignmentState()

    data class Error(
        val message: String
    ) : DeleteAssignmentState()
}