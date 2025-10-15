package com.raf.settings.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.raf.settings.data.models.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SettingsDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore("settings_preferences")

    private val settingsKey = stringPreferencesKey("settings_key")

    suspend fun saveSettings(settings: Settings) {
        try {
            val settingsString = Json.encodeToString(settings)
            context.dataStore.edit { preferences ->
                preferences[settingsKey] = settingsString
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving settings", e)
        }
    }

    fun getSettings(): Flow<Settings> {
        return context.dataStore.data.map { preferences ->
            try {
                val settingsString = preferences[settingsKey]
                if (settingsString != null) {
                    Json.decodeFromString<Settings>(settingsString)
                } else {
                    Settings()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting settings", e)
                Settings()
            }
        }
    }

    suspend fun clearSettings() {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(settingsKey)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing settings", e)
        }
    }

    private companion object {
        private const val TAG = "SettingsDataStore"
    }
}