package com.mfoumby.hassan.quran.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.mfoumby.hassan.quran.data.model.LocalSurahVersePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class SurahVersePreferencesDataStore(context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "surah_preference")
    private val store = context.dataStore
    private val surahPreferencesKey = stringPreferencesKey("surahPreferencesKey")
    private val gson = Gson()

    fun getSurahVersePreferencesFlow(): Flow<LocalSurahVersePreferences> = store.data.map { preferences ->
        preferences[surahPreferencesKey].let {
            gson.fromJson(it, LocalSurahVersePreferences::class.java)
        }
    }

    suspend fun getSurahVersePreferences(): LocalSurahVersePreferences? = store.data.firstOrNull()?.get(surahPreferencesKey)?.let {
        gson.fromJson(it, LocalSurahVersePreferences::class.java)
    }

    suspend fun setSurahVersePreferences(surahPreferences: LocalSurahVersePreferences) {
        store.updateData {
            it.toMutablePreferences().apply {
                set(surahPreferencesKey, gson.toJson(surahPreferences))
            }
        }
    }
}