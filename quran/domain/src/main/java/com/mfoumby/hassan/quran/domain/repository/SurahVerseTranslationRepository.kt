package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation

interface SurahVerseTranslationRepository {
    suspend fun getSurahVerseTranslations(surahNumber: Int, language: Language): List<SurahVerseTranslation>

    suspend fun getSurahVerseTranslationCount(language: Language): Int

    suspend fun downloadSurahVerseTranslations(language: Language)
}