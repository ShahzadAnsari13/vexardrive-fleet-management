package com.vexardrive.fleetmanager.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesManager @Inject constructor(
    private val context: Context
) {

    suspend fun saveSession(
        accessToken: String,
        refreshToken: String,
        role: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
            preferences[PreferencesKeys.USER_ROLE] = role
        }
    }
    suspend fun saveAccessToken(
        accessToken: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
        }
    }

    val accessToken: Flow<String?> =
        context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN]
        }

    val refreshToken: Flow<String?> =
        context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.REFRESH_TOKEN]
        }

    val userRole: Flow<String?> =
        context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_ROLE]
        }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}