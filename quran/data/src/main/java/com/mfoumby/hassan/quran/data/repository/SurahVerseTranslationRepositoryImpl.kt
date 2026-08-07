package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class SurahVerseTranslationRepositoryImpl(
    private val localDataSource: SurahVerseTranslationLocalDataSource,
    private val remoteDataSource: SurahVerseTranslationRemoteDataSource
): SurahVerseTranslationRepository {
    override suspend fun getSurahVerseTranslations(surahNumber: Int, language: Language): List<SurahVerseTranslation> =
        localDataSource.getSurahVerseTranslations(surahNumber, language)

    override suspend fun getSurahVerseTranslationsFromJuz(juzNumber: Int, language: Language): List<SurahVerseTranslation> =
        localDataSource.getSurahVerseTranslationsFromJuz(juzNumber, language)

    override suspend fun getSurahVerseTranslationsFromHizb(hizbNumber: Int, language: Language): List<SurahVerseTranslation> =
        localDataSource.getSurahVerseTranslationsFromHizb(hizbNumber, language)

    override suspend fun getSurahVerseTranslationCount(language: Language): Int =
        localDataSource.getSurahVerseTranslationCount(language)

    override fun downloadSurahVerseTranslations(language: Language): Flow<List<SurahVerseTranslation>> {
        return remoteDataSource.getSurahVerseTranslations(language).onEach {
            localDataSource.upsertSurahVerseTranslations(it)
        }.catch { error ->
            e("Error while downloading surah verse translations: ${error.message}")
            throw error
        }
    }

    override suspend fun deleteSurahVerseTranslation(language: Language) {
        localDataSource.deleteSurahVerseTranslation(language)
    }
}