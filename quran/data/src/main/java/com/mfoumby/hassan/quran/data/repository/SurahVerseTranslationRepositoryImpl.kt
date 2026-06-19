package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class SurahVerseTranslationRepositoryImpl(
    private val localDataSource: SurahVerseTranslationLocalDataSource,
    private val remoteDataSource: SurahVerseTranslationRemoteDataSource
): SurahVerseTranslationRepository {
    override suspend fun getSurahVerseTranslations(surahNumber: Int, language: Language): List<SurahVerseTranslation> =
        localDataSource.getSurahVerseTranslations(surahNumber, language)

    override suspend fun getSurahVerseTranslationCount(language: Language): Int =
        localDataSource.getSurahVerseTranslationCount(language)

    override fun downloadSurahVerseTranslations(language: Language): Flow<List<SurahVerseTranslation>> {
        return remoteDataSource.getSurahVerseTranslations(language).onEach {
            localDataSource.upsertSurahVerseTranslations(it)
        }
    }

    override suspend fun deleteSurahVerseTranslation(language: Language) {
        localDataSource.deleteSurahVerseTranslation(language)
    }
}