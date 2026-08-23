package com.vexardrive.fleetmanager.presentation.manager.assignment.state

import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentDto

sealed class AssignmentListState {

    data object Idle : AssignmentListState()

    data object Loading : AssignmentListState()

    data class Success(
        val assignments: List<AssignmentDto>
    ) : AssignmentListState()

    data class Error(
        val message: String
    ) : AssignmentListState()
}