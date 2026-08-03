package com.mfoumby.hassan.quran.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.quran.domain.entity.Hizb
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuranViewModel(
    private val surahRepository: SurahRepository,
    private val surahVerseRepository: SurahVerseRepository,
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(QuranState())
    val uiState: StateFlow<QuranState> = _uiState.asStateFlow()

    init {
        initSurahs()
        initAllJuz()
        initAllHizb()
        initSurahVersePreferences()
    }

    private fun initSurahs() {
        viewModelScope.launch {
            surahRepository.getSurahs().collect { surahs ->
                _uiState.update {
                    it.copy(surahs = surahs)
                }
                refreshInitializingState()
            }
        }
    }

    private fun initAllJuz() {
        viewModelScope.launch {
            surahVerseRepository.getAllJuz().collect { allJuz ->
                _uiState.update {
                    it.copy(allJuz = allJuz)
                }
                refreshInitializingState()
            }
        }
    }

    private fun initAllHizb() {
        viewModelScope.launch {
            surahVerseRepository.getAllHizb().collect { allHizb ->
                _uiState.update {
                    it.copy(allHizb = allHizb)
                }
                refreshInitializingState()
            }
        }
    }

    private fun initSurahVersePreferences() {
        viewModelScope.launch {
            surahVersePreferencesRepository.getSurahVersePreferencesFlow().collect { surahVersePreferences ->
                _uiState.update {
                    it.copy(surahVersePreferences = surahVersePreferences)
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

    data class QuranState(
        val surahs: List<Surah> = emptyList(),
        val allJuz: List<Juz> = emptyList(),
        val allHizb: List<Hizb> = emptyList(),
        val surahVersePreferences: SurahVersePreferences? = null,
        val initializing: Boolean = true
    ) {
        val initialized: Boolean
            get() = surahs.isNotEmpty() &&
                    allJuz.isNotEmpty() &&
                    allHizb.isNotEmpty() &&
                    surahVersePreferences != null
    }
}