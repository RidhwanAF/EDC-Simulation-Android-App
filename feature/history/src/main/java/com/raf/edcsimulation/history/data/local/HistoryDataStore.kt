package com.raf.edcsimulation.history.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore("history_data_preferences")

    private val lastHistoryKey = stringPreferencesKey("history_data_token_key")

    suspend fun saveLastHistory(history: String) {
        context.dataStore.edit { preferences ->
            preferences[lastHistoryKey] = history
        }
    }

    fun getLastHistory(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[lastHistoryKey]
        }
    }
}