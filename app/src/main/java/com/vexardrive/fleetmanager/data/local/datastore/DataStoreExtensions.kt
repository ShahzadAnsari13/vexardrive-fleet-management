package com.vexardrive.fleetmanager.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(
    name = "vexardrive_preferences"
)