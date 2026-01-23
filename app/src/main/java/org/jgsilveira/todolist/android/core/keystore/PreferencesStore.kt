package org.jgsilveira.todolist.android.core.keystore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class PreferencesStore(
    private val dataStore: DataStore<Preferences>
): KeyValueStore {

    override suspend fun put(key: String, value: String?) {
        dataStore.edit { preferences ->
            val prefKey = stringPreferencesKey(key)
            preferences.remove(prefKey)
            if (value != null)
                preferences[prefKey] = value
        }
    }

    override suspend fun get(key: String): String? {
        return dataStore.data.map { preferences ->
            val prefKey = stringPreferencesKey(key)
            preferences[prefKey]
        }.first()
    }

    override suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            val prefKey = stringPreferencesKey(key)
            preferences.remove(prefKey)
        }
    }

    override suspend fun keys(): Set<String> {
        return setOf()
    }

    override suspend fun values(): List<String?> {
        return listOf()
    }
}