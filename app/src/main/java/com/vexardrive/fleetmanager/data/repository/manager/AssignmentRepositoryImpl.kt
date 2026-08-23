package com.vexardrive.fleetmanager.data.repository.manager

import com.vexardrive.fleetmanager.data.remote.api.manager.AssignmentApi
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentDetailsResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentListResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.CreateAssignmentRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.CreateAssignmentResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.DeleteAssignmentResponse
import com.vexardrive.fleetmanager.domain.repository.manager.AssignmentRepository
import javax.inject.Inject

class AssignmentRepositoryImpl @Inject constructor(
    private val api: AssignmentApi
) : AssignmentRepository {

    override suspend fun createAssignment(
        request: CreateAssignmentRequest
    ): Result<CreateAssignmentResponse> {

        return try {
            val response = api.createAssignment(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response")
                )
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to create assignment"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getAssignments():
            Result<AssignmentListResponse> {

        return try {
            val response = api.getAssignments()

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response")
                )
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to fetch assignments"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getAssignmentById(
        assignmentId: String
    ): Result<AssignmentDetailsResponse> {

        return try {
            val response =
                api.getAssignmentById(assignmentId)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response")
                )
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to fetch assignment details"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun deleteAssignment(
        assignmentId: String
    ): Result<DeleteAssignmentResponse> {

        return try {
            val response =
                api.deleteAssignment(assignmentId)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(
                    Exception("Empty response")
                )
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to delete assignment"
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}