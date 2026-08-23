package com.vexardrive.fleetmanager.domain.repository.manager

import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentDetailsResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentListResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.CreateAssignmentRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.CreateAssignmentResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.DeleteAssignmentResponse

interface AssignmentRepository {

    suspend fun createAssignment(
        request: CreateAssignmentRequest
    ): Result<CreateAssignmentResponse>

    suspend fun getAssignments():
            Result<AssignmentListResponse>

    suspend fun getAssignmentById(
        assignmentId: String
    ): Result<AssignmentDetailsResponse>

    suspend fun deleteAssignment(
        assignmentId: String
    ): Result<DeleteAssignmentResponse>
}