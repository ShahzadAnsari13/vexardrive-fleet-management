package com.vexardrive.fleetmanager.presentation.manager.vehicle.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.CreateVehicleRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.UpdateVehicleStatusRequest
import com.vexardrive.fleetmanager.data.remote.dto.manager.vehicle.VehicleResponse
import com.vexardrive.fleetmanager.domain.repository.manager.VehicleRepository
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.CreateVehicleUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.UpdateVehicleStatusUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.UpdateVehicleUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.VehicleDetailUiState
import com.vexardrive.fleetmanager.presentation.manager.vehicle.state.VehicleUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _vehicles = MutableStateFlow<VehicleUiState>(VehicleUiState.Idle)
    val vehicles: StateFlow<VehicleUiState> = _vehicles

    private val _vehicleDetail =
        MutableStateFlow<VehicleDetailUiState>(VehicleDetailUiState.Idle)

    val vehicleDetail: StateFlow<VehicleDetailUiState> =
        _vehicleDetail


    private val _updateVehicle =
        MutableStateFlow<UpdateVehicleUiState>(UpdateVehicleUiState.Idle)

    val updateVehicle: StateFlow<UpdateVehicleUiState> =
        _updateVehicle


    private val _updateVehicleStatus =
        MutableStateFlow<UpdateVehicleStatusUiState>(
            UpdateVehicleStatusUiState.Idle
        )

    val updateVehicleStatus: StateFlow<UpdateVehicleStatusUiState> =
        _updateVehicleStatus


    private val _createVehicle =
        MutableStateFlow<CreateVehicleUiState>(
            CreateVehicleUiState.Idle
        )

    val createVehicle: StateFlow<CreateVehicleUiState> =
        _createVehicle
    fun getVehicles() {
        viewModelScope.launch {
            _vehicles.value = VehicleUiState.Loading

            repository.getVehicles()
                .onSuccess { response ->

                    if (response.data.isEmpty()) {
                        _vehicles.value = VehicleUiState.Empty
                    } else {
                        _vehicles.value = VehicleUiState.Success(response)
                    }

                }
                .onFailure {
                    _vehicles.value = VehicleUiState.Error(
                        it.message ?: "Failed to load vehicles"
                    )
                }
        }
    }

    fun getVehicleById(vehicleId: String) {
        viewModelScope.launch {

            _vehicleDetail.value =
                VehicleDetailUiState.Loading

            repository.getVehicleById(vehicleId)
                .onSuccess { response ->
                    _vehicleDetail.value =
                        VehicleDetailUiState.Success(response)
                }
                .onFailure {
                    _vehicleDetail.value =
                        VehicleDetailUiState.Error(
                            it.message ?: "Failed to load vehicle"
                        )
                }
        }
    }

    fun updateVehicle(
        vehicleId: String,
        request: UpdateVehicleRequest
    ) {
        viewModelScope.launch {

            _updateVehicle.value =
                UpdateVehicleUiState.Loading

            repository.updateVehicle(
                vehicleId,
                request
            )
                .onSuccess {
                    _updateVehicle.value =
                        UpdateVehicleUiState.Success(it)
                }
                .onFailure {
                    _updateVehicle.value =
                        UpdateVehicleUiState.Error(
                            it.message ?: "Failed to update vehicle"
                        )
                }
        }
    }

    fun updateVehicleStatus(
        vehicleId: String,
        status: String
    ) {
        viewModelScope.launch {

            _updateVehicleStatus.value =
                UpdateVehicleStatusUiState.Loading

            repository.updateVehicleStatus(
                vehicleId,
                UpdateVehicleStatusRequest(status)
            )
                .onSuccess {
                    _updateVehicleStatus.value =
                        UpdateVehicleStatusUiState.Success(it)
                }
                .onFailure {
                    _updateVehicleStatus.value =
                        UpdateVehicleStatusUiState.Error(
                            it.message ?: "Failed to update vehicle status"
                        )
                }
        }
    }

    fun createVehicle(request: CreateVehicleRequest) {
        viewModelScope.launch {

            _createVehicle.value =
                CreateVehicleUiState.Loading

            repository.createVehicle(request)
                .onSuccess {
                    _createVehicle.value =
                        CreateVehicleUiState.Success(it)
                }
                .onFailure {
                    _createVehicle.value =
                        CreateVehicleUiState.Error(
                            it.message ?: "Failed to create vehicle"
                        )
                }
        }
    }
}

