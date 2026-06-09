package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository

class SurahVerseTranslationRepositoryImpl(
    private val surahVerseTranslationLocalDataSource: SurahVerseTranslationLocalDataSource,
    private val surahVerseTranslationRemoteDataSource: SurahVerseTranslationRemoteDataSource
): SurahVerseTranslationRepository {
    override suspend fun getSurahVerseTranslations(surahNumber: Int, language: Language): List<SurahVerseTranslation> =
        surahVerseTranslationLocalDataSource.getSurahVerseTranslations(surahNumber, language)

    override suspend fun getSurahVerseTranslationCount(language: Language): Int =
        surahVerseTranslationLocalDataSource.getSurahVerseTranslationCount(language)

    override suspend fun downloadSurahVerseTranslations(language: Language) {
        surahVerseTranslationRemoteDataSource.getSurahVerseTranslations(language).collect {
            surahVerseTranslationLocalDataSource.upsertSurahVerseTranslations(it)
        }
    }
}