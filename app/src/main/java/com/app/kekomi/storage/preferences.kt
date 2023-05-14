package com.app.kekomi.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.collect

class userPreferences(private val context: Context) {

    // to make sure there is only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userInfo")
        var NAME_KEY = stringPreferencesKey("user_name")
        var WEIGHT_KEY = intPreferencesKey("user_weight")
        var HEIGHT_KEY = intPreferencesKey("user_height")
        var CALORIES_STATE_KEY = booleanPreferencesKey("user_calories_state")
        var SODIUM_STATE_KEY = booleanPreferencesKey("user_sodium_state")
        var SUGAR_STATE_KEY = booleanPreferencesKey("user_sugar_state")
        var FATS_STATE_KEY = booleanPreferencesKey("user_fats_state")
        var PROTEIN_STATE_KEY = booleanPreferencesKey("user_protein_state")
        var CALORIES_GOAL_KEY = intPreferencesKey("user_calories_goal")
        var SODIUM_GOAL_KEY = intPreferencesKey("user_sodium_goal")
        var SUGAR_GOAL_KEY = intPreferencesKey("user_sugar_goal")
        var FATS_GOAL_KEY = intPreferencesKey("user_fats_goal")
        var PROTEIN_GOAL_KEY = intPreferencesKey("user_protein_goal")
    }

//    val getPreferences: Flow<String?> = context.dataStore.data
//        .map { preferences ->
//            preferences[USER_EMAIL_KEY] ?: ""
//
//        }

    fun Context.readString(key: String): Flow<String> {
        return dataStore.data.map{ pref ->
            pref[stringPreferencesKey(key)] ?: ""
        }
    }

    suspend fun readStateKeys(context: Context): MutableList<Boolean> {
        val items = listOf(CALORIES_STATE_KEY, SODIUM_STATE_KEY, SUGAR_STATE_KEY, FATS_STATE_KEY, PROTEIN_STATE_KEY)
        val result = mutableListOf<Boolean>()

        for (key in items) {
            val value = context.dataStore.data.map { preferences ->
                preferences[key] ?: false
            }
            value.collect { result.add(it) }
        }

        return result
    }


        suspend fun savePreferences(key: Preferences.Key<String>,  value: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}