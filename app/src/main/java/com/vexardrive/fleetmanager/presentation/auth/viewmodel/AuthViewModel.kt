package com.vexardrive.fleetmanager.presentation.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vexardrive.fleetmanager.data.local.datastore.UserPreferencesManager
import com.vexardrive.fleetmanager.data.remote.dto.auth.LoginRequest
import com.vexardrive.fleetmanager.data.remote.dto.auth.RegisterRequest
import com.vexardrive.fleetmanager.domain.repository.AuthRepository
import com.vexardrive.fleetmanager.presentation.auth.event.AuthEvent
import com.vexardrive.fleetmanager.presentation.auth.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthState>(
        AuthState.Idle
    )
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {

            _uiState.value = AuthState.Loading

            val request = RegisterRequest(
                name = name,
                email = email,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword,
                role = "FLEET_MANAGER"
            )

            authRepository.register(request)
                .onSuccess { response ->
                    userPreferencesManager.saveSession(
                        accessToken = response.accessToken,
                        refreshToken = response.refreshToken,
                        role = "FLEET_MANAGER"
                    )
                    _uiState.value = AuthState.Idle

                    _events.emit(
                        AuthEvent.RegisterSuccess(response)
                    )
                }
                .onFailure { error ->

                    _uiState.value = AuthState.Idle

                    _events.emit(
                        AuthEvent.Error(
                            error.message ?: "Registration failed"
                        )
                    )
                }
        }
    }
    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            val request = LoginRequest(
                email = email,
                password = password
            )

            authRepository.login(request)
                .onSuccess { response ->

                    userPreferencesManager.saveSession(
                        accessToken = response.accessToken,
                        refreshToken = response.refreshToken,
                        role = response.role
                    )
                    _uiState.value = AuthState.Idle
                    _events.emit(
                        AuthEvent.LoginSuccess(response)
                    )
                }
                .onFailure { error ->
                    _uiState.value = AuthState.Idle
                    _events.emit(
                        AuthEvent.Error(
                            error.message ?: "Login failed"
                        )
                    )
                }
        }
    }
}