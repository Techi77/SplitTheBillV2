package com.stb.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.datastore by preferencesDataStore(name = "preferences")

private val USER_DATA = stringPreferencesKey("authorized_email")

class DataStoreManager (private val context: Context) {

    suspend fun setAuthorizedUser(userData: UserData){
        context.datastore.edit {preferences ->
            preferences[USER_DATA] = Json.encodeToString(userData)
        }
    }

    val getAuthorizedUserFlow: Flow<UserData?> = context.datastore.data
        .map { prefs ->
            prefs[USER_DATA]?.let { jsonString ->
                Json.decodeFromString<UserData>(jsonString)
            }
        }

    suspend fun clearUser() {
        context.datastore.edit { prefs ->
            prefs.remove(USER_DATA)
        }
    }

}