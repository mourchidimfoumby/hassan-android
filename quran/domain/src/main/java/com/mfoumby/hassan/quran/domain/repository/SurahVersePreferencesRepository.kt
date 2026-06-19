package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import kotlinx.coroutines.flow.Flow

interface SurahVersePreferencesRepository {
    fun getSurahVersePreferencesFlow(): Flow<SurahVersePreferences>

    suspend fun getSurahVersePreferences(): SurahVersePreferences?

    suspend fun setSurahVersePreferences(surahVersePreferences: SurahVersePreferences)
}