package com.wiryadev.binarbattle.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SessionPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUserSession(): Flow<UserSession> = dataStore.data.map { pref ->
        UserSession(
            username = pref[USERNAME_KEY] ?: "",
            email = pref[EMAIL_KEY] ?: "",
            token = pref[TOKEN_KEY] ?: "",
        )
    }

    suspend fun saveUserSession(user: UserSession) {
        dataStore.edit { pref ->
            pref[USERNAME_KEY] = user.username
            pref[EMAIL_KEY] = user.email
            pref[TOKEN_KEY] = user.token
        }
    }

    suspend fun deleteUserSession() {
        dataStore.edit { pref ->
            pref[USERNAME_KEY] = ""
            pref[TOKEN_KEY] = ""
            pref[EMAIL_KEY] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreference? = null

        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}