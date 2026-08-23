package com.vexardrive.fleetmanager.presentation.manager.assignment.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentDetailsDto

sealed class AssignmentDetailsState {

    data object Idle : AssignmentDetailsState()

    data object Loading : AssignmentDetailsState()

    data class Success(
        val assignment: AssignmentDetailsDto
    ) : AssignmentDetailsState()

    data class Error(
        val message: String
    ) : AssignmentDetailsState()
}