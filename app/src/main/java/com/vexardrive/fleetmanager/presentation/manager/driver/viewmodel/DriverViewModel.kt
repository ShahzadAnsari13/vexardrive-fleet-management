package com.vexardrive.fleetmanager.presentation.manager.driver.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.CreateDriverResponse
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.DriverDto
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.driver.UpdateDriverStatusRequest
import com.vexardrive.fleetmanager.domain.repository.manager.DriverRepository
import com.vexardrive.fleetmanager.presentation.manager.driver.state.CreateDriverState
import com.vexardrive.fleetmanager.presentation.manager.driver.state.DriverDetailsState
import com.vexardrive.fleetmanager.presentation.manager.driver.state.DriverListUiState
import com.vexardrive.fleetmanager.presentation.manager.driver.state.UpdateDriverState
import com.vexardrive.fleetmanager.presentation.manager.driver.state.UpdateDriverStatusState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val repository: DriverRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<DriverListUiState>(DriverListUiState.Idle)

    val uiState: StateFlow<DriverListUiState> = _uiState

    private val _createDriverState =
        MutableStateFlow<CreateDriverState>(
            CreateDriverState.Idle
        )

    val createDriverState: StateFlow<CreateDriverState> =
        _createDriverState.asStateFlow()


    private val _driverDetailsState =
        MutableStateFlow<DriverDetailsState>(
            DriverDetailsState.Idle
        )

    val driverDetailsState: StateFlow<DriverDetailsState> =
        _driverDetailsState.asStateFlow()

    private val _updateDriverState =
        MutableStateFlow<UpdateDriverState>(
            UpdateDriverState.Idle
        )

    val updateDriverState: StateFlow<UpdateDriverState> =
        _updateDriverState.asStateFlow()

    private val _updateDriverStatusState =
        MutableStateFlow<UpdateDriverStatusState>(
            UpdateDriverStatusState.Idle
        )

    val updateDriverStatusState:
            StateFlow<UpdateDriverStatusState> =
        _updateDriverStatusState.asStateFlow()
    fun getDrivers() {
        viewModelScope.launch {
            _uiState.value = DriverListUiState.Loading

            repository.getDrivers()
                .onSuccess { response ->

                    if (response.data.isEmpty()) {
                        _uiState.value = DriverListUiState.Empty
                    } else {
                        _uiState.value =
                            DriverListUiState.Success(response.data)
                    }
                }
                .onFailure { error ->
                    _uiState.value =
                        DriverListUiState.Error(
                            error.message ?: "Failed to load drivers"
                        )
                }
        }
    }
    fun createDriver(request: CreateDriverRequest) {
        viewModelScope.launch {

            _createDriverState.value = CreateDriverState.Loading

            repository.createDriver(request)
                .onSuccess { response ->
                    _createDriverState.value =
                        CreateDriverState.Success(response)
                }
                .onFailure { error ->
                    _createDriverState.value =
                        CreateDriverState.Error(
                            error.message ?: "Failed to create driver"
                        )
                }
        }
    }

    fun getDriverById(driverId: String) {
        viewModelScope.launch {

            _driverDetailsState.value =
                DriverDetailsState.Loading

            repository.getDriverById(driverId)
                .onSuccess { response ->
                    _driverDetailsState.value =
                        DriverDetailsState.Success(response.data)
                }
                .onFailure { error ->
                    _driverDetailsState.value =
                        DriverDetailsState.Error(
                            error.message ?: "Failed to fetch driver details"
                        )
                }
        }
    }
    fun updateDriver(
        driverId: String,
        request: UpdateDriverRequest
    ) {
        viewModelScope.launch {

            _updateDriverState.value =
                UpdateDriverState.Loading

            repository.updateDriver(driverId, request)
                .onSuccess { response ->
                    _updateDriverState.value =
                        UpdateDriverState.Success(response)
                }
                .onFailure { error ->
                    _updateDriverState.value =
                        UpdateDriverState.Error(
                            error.message
                                ?: "Failed to update driver"
                        )
                }
        }
    }

    fun updateDriverStatus(
        driverId: String,
        status: String
    ) {
        viewModelScope.launch {

            _updateDriverStatusState.value =
                UpdateDriverStatusState.Loading

            repository.updateDriverStatus(
                driverId,
                UpdateDriverStatusRequest(status)
            ).onSuccess { response ->

                _updateDriverStatusState.value =
                    UpdateDriverStatusState.Success(response)

            }.onFailure { error ->

                _updateDriverStatusState.value =
                    UpdateDriverStatusState.Error(
                        error.message ?: "Failed to update driver status"
                    )
            }
        }
    }
}