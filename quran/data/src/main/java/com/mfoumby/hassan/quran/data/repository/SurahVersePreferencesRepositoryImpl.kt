package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.quran.data.local.SurahVersePreferencesLocalDataSource
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import kotlinx.coroutines.flow.Flow

class SurahVersePreferencesRepositoryImpl(
    private val surahPreferencesLocalDataSource: SurahVersePreferencesLocalDataSource
): SurahVersePreferencesRepository {
    override fun getSurahVersePreferencesFlow(): Flow<SurahVersePreferences> =
        surahPreferencesLocalDataSource.getSurahVersePreferencesFlow()

    override suspend fun getSurahVersePreferences(): SurahVersePreferences? =
        surahPreferencesLocalDataSource.getSurahVersePreferences()

    override suspend fun setSurahVersePreferences(surahVersePreferences: SurahVersePreferences) {
        surahPreferencesLocalDataSource.setSurahVersePreferences(surahVersePreferences)
    }
}