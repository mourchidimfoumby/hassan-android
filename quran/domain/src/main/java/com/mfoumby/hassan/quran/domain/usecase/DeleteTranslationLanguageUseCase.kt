package com.mfoumby.hassan.quran.domain.usecase

import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository

class DeleteTranslationLanguageUseCase(
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository,
    private val surahVerseTranslationLanguageRepository: SurahVerseTranslationLanguageRepository,
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository
) {
    suspend fun execute(translationLanguage: TranslationLanguage) {
        surahVerseTranslationRepository.deleteSurahVerseTranslation(translationLanguage.language)
        surahVerseTranslationLanguageRepository.updateTranslationLanguage(
            translationLanguage.copy(state = TranslationLanguageState.NotDownloaded)
        )
        surahVersePreferencesRepository.getSurahVersePreferences()?.let {
            if (it.translationLanguage == translationLanguage.language) {
                surahVersePreferencesRepository.setSurahVersePreferences(it.copy(translationLanguage = null))
            }
        }
    }
}