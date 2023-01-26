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

class SessionManager (private val context: Context) {

    val user_token: Flow<String?>
        get() = context.dataStore.data.map { pref ->
            pref[USER_TOKEN]
        }

    val role_id: Flow<String?>
        get() = context.dataStore.data.map { pref ->
            pref[ROLE_ID]
        }

    val user_email: Flow<String?>
        get() = context.dataStore.data.map { pref ->
            pref[USER_EMAIL]
        }

    val session_activity: Flow<String?>
        get() = context.dataStore.data.map { pref ->
            pref[SESSION_ACTIVITY]
        }

    suspend fun saveAuthToken(user_token: String, role_id: String) {
        context.dataStore.edit { pref ->
            pref[USER_TOKEN] = user_token
            pref[ROLE_ID] = role_id
        }
    }

    suspend fun rememberMe(user_email: String) {
        context.dataStore.edit { pref ->
            pref[USER_EMAIL] = user_email
        }
    }

    suspend fun clearRememberMe(){
        context.dataStore.edit { pref ->
            pref.remove(USER_EMAIL)
        }
    }

    suspend fun clearUserToken() {
        context.dataStore.edit { pref ->
            pref.remove(USER_TOKEN)
            pref.remove(ROLE_ID)
        }
    }

    suspend fun sessionActivity(session: String) {
        context.dataStore.edit { pref ->
            pref[SESSION_ACTIVITY] = session
        }
    }

    suspend fun clearSessionActivity() {
        context.dataStore.edit { pref ->
            pref.remove(SESSION_ACTIVITY)
        }
    }

    companion object {
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_TOKEN = stringPreferencesKey("user_token")
        private val DEVICE_TOKEN = stringPreferencesKey("device_token")
        private val ROLE_ID = stringPreferencesKey("role_id")
        private val SESSION_ACTIVITY = stringPreferencesKey("session_activity")

    }
}