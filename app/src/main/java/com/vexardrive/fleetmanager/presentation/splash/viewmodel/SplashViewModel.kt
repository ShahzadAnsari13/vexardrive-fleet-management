package com.vexardrive.fleetmanager.presentation.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vexardrive.fleetmanager.data.local.datastore.UserPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {

    fun checkUserSession(
        onResult: (String?) -> Unit
    ) {
        viewModelScope.launch {

            delay(1000)

            val token = userPreferencesManager.accessToken.first()
            val role = userPreferencesManager.userRole.first()

            if (!token.isNullOrEmpty() && !role.isNullOrEmpty()) {
                onResult(role)
            } else {
                onResult(null)
            }
        }
    }
}