package dev.iconpln.mims.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_sessions")

class TokenManager(private val context: Context) {

    val user_token: Flow<String?>
        get() = context.dataStore.data.map { pref ->
            pref[USER_TOKEN]
        }

    val device_token: Flow<String?>
        get() = context.dataStore.data.map { pref ->
            pref[DEVICE_TOKEN]
        }

    val role_id: Flow<String?>
        get() = context.dataStore.data.map { pref ->
            pref[ROLE_ID]
        }

    suspend fun saveAuthToken(user_token: String, device_token: String, role_id: String) {
        context.dataStore.edit { pref ->
            pref[USER_TOKEN] = user_token
            pref[DEVICE_TOKEN] = device_token
            pref[ROLE_ID] = role_id
        }
    }

    suspend fun clearUserToken() {
        context.dataStore.edit { pref ->
            pref.remove(USER_TOKEN)
            pref.remove(ROLE_ID)
//            pref.remove(DEVICE_TOKEN)
        }
    }

    companion object {
        private val USER_TOKEN = stringPreferencesKey("user_token")
        private val DEVICE_TOKEN = stringPreferencesKey("device_token")
        private val ROLE_ID = stringPreferencesKey("role_id")
    }
}