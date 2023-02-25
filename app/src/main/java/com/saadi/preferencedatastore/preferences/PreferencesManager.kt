package com.saadi.preferencedatastore.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesManager private constructor(context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: PreferencesManager? = null

        fun getInstance(context: Context): PreferencesManager {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = PreferencesManager(context)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
        context.applicationContext.preferencesDataStoreFile("my_preferences")
    }

    suspend fun save(key: String, value: Any?) {
        dataStore.edit { preferences ->
            when (value) {
                is String -> preferences[stringPreferencesKey(key)] = value
                is Int -> preferences[intPreferencesKey(key)] = value
                is Long -> preferences[longPreferencesKey(key)] = value
                is Float -> preferences[floatPreferencesKey(key)] = value
                is Boolean -> preferences[booleanPreferencesKey(key)] = value
                else -> throw IllegalArgumentException("Unsupported preference type")
            }
        }
    }

    fun getString(key: String, defaultValue: String? = null): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: defaultValue
        }
    }

    fun getInt(key: String, defaultValue: Int = 0): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[intPreferencesKey(key)] ?: defaultValue
        }
    }

    fun getLong(key: String, defaultValue: Long = 0L): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[longPreferencesKey(key)] ?: defaultValue
        }
    }

    fun getFloat(key: String, defaultValue: Float = 0f): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[floatPreferencesKey(key)] ?: defaultValue
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: defaultValue
        }
    }

    suspend fun remove(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
            preferences.remove(intPreferencesKey(key))
            preferences.remove(longPreferencesKey(key))
            preferences.remove(floatPreferencesKey(key))
            preferences.remove(booleanPreferencesKey(key))
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}