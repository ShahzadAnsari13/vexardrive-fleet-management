package com.vexardrive.fleetmanager.data.local.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {

    val ACCESS_TOKEN = stringPreferencesKey("access_token")

    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

    val USER_ROLE = stringPreferencesKey("user_role")
}