package com.vexardrive.fleetmanager.presentation.manager.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vexardrive.fleetmanager.data.remote.dto.manager.dashboard.DashboardResponse
import com.vexardrive.fleetmanager.domain.repository.manager.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.onFailure
sealed interface DashboardState {

    data object Idle : DashboardState

    data object Loading : DashboardState

    data class Success(
        val data: DashboardResponse
    ) : DashboardState

    data class Error(
        val message: String
    ) : DashboardState
}
@HiltViewModel
class ManagerDashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {
    private val _state = MutableStateFlow<DashboardState>(
        DashboardState.Idle
    )

    val state: StateFlow<DashboardState> =
        _state.asStateFlow()
    fun getDashboard() {
        println("🔥 DASHBOARD API CALL STARTED")

        viewModelScope.launch {

            _state.value = DashboardState.Loading

            dashboardRepository.getDashboard()
                .onSuccess { dashboard ->

                    _state.value = DashboardState.Success(
                        data = dashboard
                    )

                }
                .onFailure { error ->

                    _state.value = DashboardState.Error(
                        message = error.message
                            ?: "Failed to load dashboard"
                    )
                }
        }
    }
}