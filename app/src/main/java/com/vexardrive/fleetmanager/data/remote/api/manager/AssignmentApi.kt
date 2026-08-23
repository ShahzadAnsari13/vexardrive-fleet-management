package com.vexardrive.fleetmanager.data.remote.api.manager

import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentDetailsResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.AssignmentListResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.CreateAssignmentRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.CreateAssignmentResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.DeleteAssignmentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AssignmentApi {
    @POST("assignments")
    suspend fun createAssignment(
        @Body request: CreateAssignmentRequest
    ): Response<CreateAssignmentResponse>

    @GET("assignments")
    suspend fun getAssignments(): Response<AssignmentListResponse>

    @GET("assignments/{id}")
    suspend fun getAssignmentById(
        @Path("id") assignmentId: String
    ): Response<AssignmentDetailsResponse>

    @DELETE("assignments/{id}")
    suspend fun deleteAssignment(
        @Path("id") assignmentId: String
    ): Response<DeleteAssignmentResponse>
}