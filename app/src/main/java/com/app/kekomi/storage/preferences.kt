package com.app.kekomi.storage

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.capitalize
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
        var WEIGHT_KEY = stringPreferencesKey("user_weight")
        var HEIGHT_KEY = stringPreferencesKey("user_height")
        var SCANNED_VALUE_KEY = stringPreferencesKey("scanned_value")
        var CALORIES_STATE_KEY = booleanPreferencesKey("user_calories_state")
        var SODIUM_STATE_KEY = booleanPreferencesKey("user_sodium_state")
        var SUGAR_STATE_KEY = booleanPreferencesKey("user_sugar_state")
        var FATS_STATE_KEY = booleanPreferencesKey("user_fats_state")
        var PROTEIN_STATE_KEY = booleanPreferencesKey("user_proteins_state")
        var CALORIES_GOAL_KEY = stringPreferencesKey("user_calories_goal")
        var SODIUM_GOAL_KEY = stringPreferencesKey("user_sodium_goal")
        var SUGAR_GOAL_KEY = stringPreferencesKey("user_sugar_goal")
        var FATS_GOAL_KEY = stringPreferencesKey("user_fats_goal")
        var PROTEIN_GOAL_KEY = stringPreferencesKey("user_proteins_goal")
        val statesKeys = listOf(CALORIES_STATE_KEY, SODIUM_STATE_KEY, SUGAR_STATE_KEY, FATS_STATE_KEY, PROTEIN_STATE_KEY)
        val goalKeys = listOf(CALORIES_GOAL_KEY, SODIUM_GOAL_KEY, SUGAR_GOAL_KEY, FATS_GOAL_KEY, PROTEIN_GOAL_KEY)
    }

    val getName: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[NAME_KEY] ?: ""
        }

    // to save the email
    suspend fun saveName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }

    suspend fun saveScannedValue(scannedValue: String){
        context.dataStore.edit { preferences ->
            preferences[SCANNED_VALUE_KEY] = scannedValue
        }
    }

    val getScannedValue: Flow<String> = context.dataStore.data
        .map{ preferences ->
            preferences[SCANNED_VALUE_KEY]?:""
        }

    val getHeight: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[HEIGHT_KEY] ?: ""
        }

    // to save the email
    suspend fun saveHeight(h: String) {
        context.dataStore.edit { preferences ->
            preferences[HEIGHT_KEY] = h
        }
    }

    val getWeight: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[WEIGHT_KEY] ?: ""
        }

    // to save the email
    suspend fun saveWeight(w: String) {
        context.dataStore.edit { preferences ->
            preferences[WEIGHT_KEY] = w
        }
    }

    fun getGoalFromKey(key: String)= context.dataStore.data
        .map { preferences ->
            preferences[stringPreferencesKey("user_" + key.lowercase() + "_goal")] ?: ""
        }


    fun getItems(): List<String> {
        return statesKeys.map { key ->
            key.toString().substringAfter("user_").substringBefore("_state").capitalize()
        }
    }
    fun Context.readString(key: String): Flow<String> {
        return dataStore.data.map{ pref ->
            pref[stringPreferencesKey(key)] ?: ""
        }
     }

    suspend fun createEmptyGoals (): List<String> {
        val size = goalKeys.size
        val emptyList = List(size) { "0" }
        saveGoals(emptyList as SnapshotStateList<String>)
        return emptyList
    }

    val getStates: List<Flow<Boolean>> = statesKeys.map { item ->
        context.dataStore.data.map { preferences ->
            preferences[item] ?: false
        }
    }
    suspend fun saveStates(checkedItems: SnapshotStateList<Boolean>) {

        for(key in statesKeys){
            val value = checkedItems[statesKeys.indexOf(key)]
            context.dataStore.edit{ preferences ->
                preferences[key] = value
            }

        }
    }

    val getGoals: List<Flow<String>> = goalKeys.map { item ->
        context.dataStore.data.map { preferences ->
            preferences[item] ?: "0"
        }
    }

    suspend fun saveGoals(goalItems: SnapshotStateList<String>) {

        for(key in goalKeys){
            val value = goalItems[goalKeys.indexOf(key)]
            context.dataStore.edit{ preferences ->
                preferences[key] = value
            }
        }
    }

    suspend fun saveGoal(value: String, index: Int) {
        val key = goalKeys[index]
        context.dataStore.edit{ preferences ->
            preferences[key] = value
        }
    }

    fun getGoal(index: Int): Flow<String?> {
        val key = goalKeys[index]
        val value = context.dataStore.data.map { preferences ->
            preferences[key] ?: ""
        }
        return value
    }

    val getFatsGoal: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[FATS_GOAL_KEY] ?: ""
        }

}