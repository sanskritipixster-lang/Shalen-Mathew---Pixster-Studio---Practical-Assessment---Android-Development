package com.example.practise.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesManager(private val context: Context) {

    companion object {
        private val GET_STARTED_KEY = booleanPreferencesKey("get_started_clicked")
    }

    val isGetStartedClicked: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[GET_STARTED_KEY] ?: false
        }

    suspend fun setGetStartedClicked(clicked: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[GET_STARTED_KEY] = clicked
        }
    }
}
