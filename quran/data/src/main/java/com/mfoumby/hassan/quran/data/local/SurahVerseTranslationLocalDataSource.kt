package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.mapper.toLocal
import com.mfoumby.hassan.quran.data.mapper.toSurahVerseTranslation
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation

class SurahVerseTranslationLocalDataSource(private val surahVerseTranslationDao: SurahVerseTranslationDao) {
    suspend fun getSurahVerseTranslations(surahNumber: Int, language: Language): List<SurahVerseTranslation> =
        surahVerseTranslationDao.getSurahVerseTranslations(surahNumber, language.getLanguageCode()).map { it.toSurahVerseTranslation() }

    suspend fun getSurahVerseTranslationCount(language: Language): Int =
        surahVerseTranslationDao.getSurahVerseTranslationCount(language.getLanguageCode())

    suspend fun upsertSurahVerseTranslations(surahVerseTranslations: List<SurahVerseTranslation>) =
        surahVerseTranslationDao.upsertSurahVerseTranslations(surahVerseTranslations.map { it.toLocal() })
}