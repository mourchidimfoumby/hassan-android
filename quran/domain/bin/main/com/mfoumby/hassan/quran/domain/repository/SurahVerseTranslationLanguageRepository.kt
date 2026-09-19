package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import kotlinx.coroutines.flow.Flow

interface SurahVerseTranslationLanguageRepository {
    fun getTranslationLanguagesFlow(): Flow<List<TranslationLanguage>>

    suspend fun getTranslationLanguages(): List<TranslationLanguage>

    suspend fun fetchTranslationLanguages(): List<Language>

    suspend fun setTranslationLanguages(translationLanguages: List<TranslationLanguage>)

    suspend fun updateTranslationLanguage(translationLanguage: TranslationLanguage)
}