package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.mapper.toLocalSurahVersePreferences
import com.mfoumby.hassan.quran.data.mapper.toSurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SurahVersePreferencesLocalDataSource(private val surahVersePreferencesDataStore: SurahVersePreferencesDataStore) {
    private val dispatcher = Dispatchers.IO

    fun getSurahVersePreferencesFlow(): Flow<SurahVersePreferences> =
        surahVersePreferencesDataStore.getSurahVersePreferencesFlow().map {
            it.toSurahVersePreferences()
        }

    suspend fun getSurahVersePreferences(): SurahVersePreferences? =
        surahVersePreferencesDataStore.getSurahVersePreferences()?.toSurahVersePreferences()
    
    suspend fun setSurahVersePreferences(surahVersePreferences: SurahVersePreferences) {
        withContext(dispatcher) {
            surahVersePreferencesDataStore.setSurahVersePreferences(surahVersePreferences.toLocalSurahVersePreferences())
        }
    }
}