package dev.iconpln.mims.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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

    val username: String
        get() = context.dataStore.data.map { pref ->
            pref[USERNAME]
        }.toString()

    val password: String
        get() = context.dataStore.data.map { pref ->
            pref[PASSWORD]
        }.toString()

    val is_login_biometric: Flow<Int?>
        get() = context.dataStore.data.map { pref ->
            pref[IS_LOGIN_BIOMETRIC]
        }

    suspend fun saveAuthToken(user_token: String, role_id: String, email: String, kdUser: String) {
        context.dataStore.edit { pref ->
            pref[USER_TOKEN] = user_token
            pref[ROLE_ID] = role_id
            pref[USER_EMAIL] = email
            pref[KD_USER] = kdUser
        }
    }

    suspend fun clearSaveAuthToken() {
        context.dataStore.edit { pref ->
            pref.remove(USER_TOKEN)
            pref.remove(ROLE_ID)
            pref.remove(USER_EMAIL)
            pref.remove(KD_USER)
        }
    }

    suspend fun saveUsernamePassword(username: String,password: String) {
        context.dataStore.edit { pref ->
            pref[USERNAME] = username
            pref[PASSWORD] = password
        }
    }

    suspend fun isLoginBiometric(isLoginBiometric: Int) {
        context.dataStore.edit { pref ->
            pref[IS_LOGIN_BIOMETRIC] = isLoginBiometric
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
        private val KD_USER = stringPreferencesKey("kd_user")
        private val ROLE_ID = stringPreferencesKey("role_id")
        private val USERNAME = stringPreferencesKey("username")
        private val PASSWORD = stringPreferencesKey("password")
        private val IS_LOGIN_BIOMETRIC = intPreferencesKey("is_login_biometric")
        private val SESSION_ACTIVITY = stringPreferencesKey("session_activity")

    }
}