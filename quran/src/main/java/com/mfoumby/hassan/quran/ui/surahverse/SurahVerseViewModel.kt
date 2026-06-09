package com.mfoumby.hassan.quran.ui.surahverse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.common.domain.usecase.LanguageUseCase
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SurahVerseViewModel(
    private val surahNumber: Int,
    private val surahRepository: SurahRepository,
    private val surahVerseRepository: SurahVerseRepository,
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository,
    private val languageUseCase: LanguageUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SurahVerseUiState())
    val uiState: StateFlow<SurahVerseUiState> = _uiState.asStateFlow()

    init {
        initSurah()
        initSurahVerses()
    }

    private fun initSurah() {
        viewModelScope.launch {
            val surah = surahRepository.getSurah(surahNumber)
            _uiState.update {
                it.copy(surah = surah)
            }
        }
    }

    private fun initSurahVerses() {
        viewModelScope.launch {
            var surahVerses = surahVerseRepository.getSurahVerses(surahNumber)
            val surahVerseTranslations = surahVerseTranslationRepository.getSurahVerseTranslations(surahNumber, languageUseCase.getCurrentLanguage())
                .sortedBy { it.number }

            if (surahVerseTranslations.isNotEmpty()) {
                surahVerses = surahVerses.mapIndexed { index, surahVerse ->
                    surahVerse.copy(translation = surahVerseTranslations[index].translation)
                }
            }

            _uiState.update {
                it.copy(surahVerses = surahVerses)
            }
        }
    }

    data class SurahVerseUiState(
        val surah: Surah? = null,
        val surahVerses: List<SurahVerse> = emptyList(),
        val arabicFontSize: Int = 22
    )
}