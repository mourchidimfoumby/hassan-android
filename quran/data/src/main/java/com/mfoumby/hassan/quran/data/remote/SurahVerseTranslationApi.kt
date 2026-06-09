package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.quran.data.model.RemoteSurahVerseTranslation
import kotlinx.coroutines.flow.Flow

interface SurahVerseTranslationApi {
    fun getAllSurahVerseTranslations(language: String): Flow<List<RemoteSurahVerseTranslation>>

    suspend fun getSurahVerseTranslations(surahNumber: Int, language: String): List<RemoteSurahVerseTranslation>
}