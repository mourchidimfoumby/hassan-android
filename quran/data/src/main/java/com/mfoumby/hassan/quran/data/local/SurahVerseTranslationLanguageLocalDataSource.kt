package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.common.data.mapper.toLocal
import com.mfoumby.hassan.common.data.mapper.toTranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SurahVerseTranslationLanguageLocalDataSource(
    private val translationLanguageDataStore: SurahVerseTranslationLanguageDataStore
) {
    fun getTranslationLanguagesFlow(): Flow<List<TranslationLanguage>> =
        translationLanguageDataStore.getTranslationLanguagesFlow().map { translationLanguages ->
            translationLanguages.map {
                it.toTranslationLanguage()
            }
        }

    suspend fun getTranslationLanguages(): List<TranslationLanguage> =
        translationLanguageDataStore.getTranslationLanguages().map {
            it.toTranslationLanguage()
        }

    suspend fun setTranslationLanguages(translationLanguages: List<TranslationLanguage>) {
        translationLanguageDataStore.setTranslationLanguages(translationLanguages.map { it.toLocal() })
    }

    suspend fun updateTranslationLanguage(translationLanguage: TranslationLanguage) {
        translationLanguageDataStore.updateTranslationLanguage(translationLanguage.toLocal())
    }
}