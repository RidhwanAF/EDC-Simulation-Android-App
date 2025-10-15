package com.raf.edcsimulation.auth.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore("auth_data_preferences")

    private val jwtTokenKey = stringPreferencesKey("jwt_token_key")

    suspend fun saveJwtToken(token: String) {
        try {
            context.dataStore.edit { preferences ->
                preferences[jwtTokenKey] = token
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving JWT token", e)
        }
    }

    fun getJwtToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            try {
                preferences[jwtTokenKey]
            } catch (e: Exception) {
                Log.e(TAG, "Error getting JWT token", e)
                null
            }
        }
    }

    suspend fun clearJwtToken() {
        try {
            context.dataStore.edit { preferences ->
                preferences.remove(jwtTokenKey)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing JWT token", e)
        }
    }

    private companion object {
        private const val TAG = "AuthDataStore"
    }
}