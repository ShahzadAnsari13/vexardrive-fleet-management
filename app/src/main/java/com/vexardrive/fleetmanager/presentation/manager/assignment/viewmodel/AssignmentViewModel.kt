package com.vexardrive.fleetmanager.presentation.manager.assignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vexardrive.fleetmanager.data.remote.dto.manager.assignment.CreateAssignmentRequest
import com.vexardrive.fleetmanager.domain.repository.manager.AssignmentRepository
import com.vexardrive.fleetmanager.presentation.manager.assignment.state.AssignmentDetailsState
import com.vexardrive.fleetmanager.presentation.manager.assignment.state.AssignmentListState
import com.vexardrive.fleetmanager.presentation.manager.assignment.state.CreateAssignmentState
import com.vexardrive.fleetmanager.presentation.manager.assignment.state.DeleteAssignmentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignmentViewModel @Inject constructor(
    private val repository: AssignmentRepository
) : ViewModel() {

    private val _assignmentListState =
        MutableStateFlow<AssignmentListState>(
            AssignmentListState.Idle
        )

    val assignmentListState:
            StateFlow<AssignmentListState> =
        _assignmentListState.asStateFlow()


    private val _assignmentDetailsState =
        MutableStateFlow<AssignmentDetailsState>(
            AssignmentDetailsState.Idle
        )

    val assignmentDetailsState:
            StateFlow<AssignmentDetailsState> =
        _assignmentDetailsState.asStateFlow()


    private val _createAssignmentState =
        MutableStateFlow<CreateAssignmentState>(
            CreateAssignmentState.Idle
        )

    val createAssignmentState:
            StateFlow<CreateAssignmentState> =
        _createAssignmentState.asStateFlow()


    private val _deleteAssignmentState =
        MutableStateFlow<DeleteAssignmentState>(
            DeleteAssignmentState.Idle
        )

    val deleteAssignmentState:
            StateFlow<DeleteAssignmentState> =
        _deleteAssignmentState.asStateFlow()


    fun getAssignments() {
        viewModelScope.launch {
            _assignmentListState.value =
                AssignmentListState.Loading

            repository.getAssignments()
                .onSuccess { response ->
                    _assignmentListState.value =
                        AssignmentListState.Success(response.data)
                }
                .onFailure { error ->
                    _assignmentListState.value =
                        AssignmentListState.Error(
                            error.message ?: "Failed to fetch assignments"
                        )
                }
        }
    }


    fun getAssignmentById(assignmentId: String) {
        viewModelScope.launch {
            _assignmentDetailsState.value =
                AssignmentDetailsState.Loading

            repository.getAssignmentById(assignmentId)
                .onSuccess { response ->
                    _assignmentDetailsState.value =
                        AssignmentDetailsState.Success(response.data)
                }
                .onFailure { error ->
                    _assignmentDetailsState.value =
                        AssignmentDetailsState.Error(
                            error.message ?: "Failed to fetch assignment"
                        )
                }
        }
    }


    fun createAssignment(
        request: CreateAssignmentRequest
    ) {
        viewModelScope.launch {
            _createAssignmentState.value =
                CreateAssignmentState.Loading

            repository.createAssignment(request)
                .onSuccess { response ->
                    _createAssignmentState.value =
                        CreateAssignmentState.Success(response)
                }
                .onFailure { error ->
                    _createAssignmentState.value =
                        CreateAssignmentState.Error(
                            error.message ?: "Failed to create assignment"
                        )
                }
        }
    }


    fun deleteAssignment(assignmentId: String) {
        viewModelScope.launch {
            _deleteAssignmentState.value =
                DeleteAssignmentState.Loading

            repository.deleteAssignment(assignmentId)
                .onSuccess { response ->
                    _deleteAssignmentState.value =
                        DeleteAssignmentState.Success(response)
                }
                .onFailure { error ->
                    _deleteAssignmentState.value =
                        DeleteAssignmentState.Error(
                            error.message ?: "Failed to delete assignment"
                        )
                }
        }
    }
}