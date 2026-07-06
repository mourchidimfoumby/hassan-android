package com.mfoumby.hassan.quran.data.extension

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

inline fun <reified T> DataStore<Preferences>.getJsonFlow(
    key: Preferences.Key<String>,
    gson: Gson = Gson()
): Flow<T?> {
    val type = object : TypeToken<T>() {}.type
    return data.map { preferences ->
        preferences[key]?.let {
            gson.fromJson(it, type)
        }
    }
}

suspend inline fun <reified T> DataStore<Preferences>.getJson(
    key: Preferences.Key<String>,
    gson: Gson = Gson()
): T? {
    val type = object : TypeToken<T>() {}.type
    return data.firstOrNull()?.get(key)?.let {
        gson.fromJson(it, type)
    }
}

suspend inline fun <reified T> DataStore<Preferences>.setJson(
    key:  Preferences.Key<String>,
    value: T,
    gson: Gson = Gson()
) {
    updateData {
        it.toMutablePreferences().apply {
            set(key, gson.toJson(value))
        }
    }
}