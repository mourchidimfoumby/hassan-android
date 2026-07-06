package com.mfoumby.hassan.quran.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mfoumby.hassan.quran.data.extension.getJson
import com.mfoumby.hassan.quran.data.extension.getJsonFlow
import com.mfoumby.hassan.quran.data.extension.setJson
import com.mfoumby.hassan.quran.data.model.LocalSurahVersePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class SurahVersePreferencesDataStore(context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("surah_preference")
    private val store = context.dataStore
    private val surahVersePreferencesKey = stringPreferencesKey("surahVersePreferencesKey")

    fun getSurahVersePreferencesFlow(): Flow<LocalSurahVersePreferences> =
        store.getJsonFlow<LocalSurahVersePreferences>(surahVersePreferencesKey).filterNotNull()

    suspend fun getSurahVersePreferences(): LocalSurahVersePreferences? = store.getJson(surahVersePreferencesKey)

    suspend fun setSurahVersePreferences(surahPreferences: LocalSurahVersePreferences) {
        store.setJson(surahVersePreferencesKey, surahPreferences)
    }
}