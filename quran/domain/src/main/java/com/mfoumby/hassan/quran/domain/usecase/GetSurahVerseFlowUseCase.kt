package com.mfoumby.hassan.quran.domain.usecase

import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flow

class GetSurahVerseFlowUseCase(
    private val surahVerseRepository: SurahVerseRepository,
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository,
    private val surahPreferencesRepository: SurahVersePreferencesRepository
) {
    fun execute(surahNumber: Int): Flow<List<SurahVerse>> {
        return combine(
            flow { emit(surahVerseRepository.getSurahVerses(surahNumber)) },
            surahPreferencesRepository.getSurahVersePreferencesFlow().distinctUntilChangedBy { it.translationLanguage }
        ) { surahVerses, preferences ->
            val surahVerseTranslations = preferences.translationLanguage?.let { language ->
                surahVerseTranslationRepository.getSurahVerseTranslations(surahNumber, language).sortedBy { it.number }
            } ?: emptyList()

            if (surahVerseTranslations.isNotEmpty()) {
                surahVerses.mapIndexed { index, surahVerse ->
                    surahVerse.copy(translation = surahVerseTranslations[index].translation)
                }
            } else {
                surahVerses
            }
        }
    }
}