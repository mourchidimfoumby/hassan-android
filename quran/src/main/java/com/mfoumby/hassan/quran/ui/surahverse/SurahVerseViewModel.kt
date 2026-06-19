package com.mfoumby.hassan.quran.ui.surahverse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.usecase.GetSurahVerseFlowUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SurahVerseViewModel(
    private val surahNumber: Int,
    private val surahRepository: SurahRepository,
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository,
    private val getSurahVerseFlowUseCase: GetSurahVerseFlowUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SurahVerseUiState())
    val uiState: StateFlow<SurahVerseUiState> = _uiState.asStateFlow()

    init {
        initialize()
    }

    fun onDisplayTranslationChange(displayTranslation: Boolean) {
        val surahPreferences = uiState.value.surahVersePreferences ?: return
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(surahPreferences.copy(displayTranslation = displayTranslation))
        }
    }

    private fun initialize() {
        initSurah()
        initSurahVerses()
        initSurahVersePreferences()
    }

    private fun initSurah() {
        viewModelScope.launch {
            val surah = surahRepository.getSurah(surahNumber)
            _uiState.update {
                it.copy(surah = surah)
            }
            refreshInitializingState()
        }
    }

    private fun initSurahVerses() {
        viewModelScope.launch {
            getSurahVerseFlowUseCase.execute(surahNumber).collect { surahVerses ->
                _uiState.update {
                    it.copy(surahVerses = surahVerses)
                }
                refreshInitializingState()
            }
        }
    }

    private fun initSurahVersePreferences() {
        viewModelScope.launch {
            surahVersePreferencesRepository.getSurahVersePreferencesFlow().collect { surahPreferences ->
                _uiState.update {
                    it.copy(surahVersePreferences = surahPreferences)
                }
                refreshInitializingState()
            }
        }
    }

    private fun refreshInitializingState() {
        if (!uiState.value.initializing) return
        _uiState.update {
            it.copy(initializing = !it.initialized)
        }
    }

    data class SurahVerseUiState(
        val surah: Surah? = null,
        val surahVerses: List<SurahVerse> = emptyList(),
        val surahVersePreferences: SurahVersePreferences? = null,
        val initializing: Boolean = true
    ) {
        val initialized: Boolean
            get() = surah != null &&
                    surahVerses.isNotEmpty() &&
                    surahVersePreferences != null
    }
}