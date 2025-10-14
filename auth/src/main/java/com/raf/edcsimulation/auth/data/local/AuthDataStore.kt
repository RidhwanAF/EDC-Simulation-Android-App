package com.raf.edcsimulation.auth.data.local

import android.content.Context
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
        context.dataStore.edit { preferences ->
            preferences[jwtTokenKey] = token
        }
    }

    fun getJwtToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[jwtTokenKey]
        }
    }

    suspend fun clearJwtToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(jwtTokenKey)
        }
    }
}