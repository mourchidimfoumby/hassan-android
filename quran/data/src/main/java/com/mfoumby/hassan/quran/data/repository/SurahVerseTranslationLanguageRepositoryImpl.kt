package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.quran.data.local.SurahVerseTranslationLanguageLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseTranslationLanguageRemoteDataSource
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import kotlinx.coroutines.flow.Flow

class SurahVerseTranslationLanguageRepositoryImpl(
    private val localDataSource: SurahVerseTranslationLanguageLocalDataSource,
    private val remoteDataSource: SurahVerseTranslationLanguageRemoteDataSource
): SurahVerseTranslationLanguageRepository {
    override fun getTranslationLanguagesFlow(): Flow<List<TranslationLanguage>> =
        localDataSource.getTranslationLanguagesFlow()

    override suspend fun getTranslationLanguages(): List<TranslationLanguage> =
        localDataSource.getTranslationLanguages()

    override suspend fun fetchTranslationLanguages(): List<Language> = remoteDataSource.getTranslationLanguages()

    override suspend fun setTranslationLanguages(translationLanguages: List<TranslationLanguage>) {
        localDataSource.setTranslationLanguages(translationLanguages)
    }

    override suspend fun updateTranslationLanguage(translationLanguage: TranslationLanguage) {
        localDataSource.updateTranslationLanguage(translationLanguage)
    }
}