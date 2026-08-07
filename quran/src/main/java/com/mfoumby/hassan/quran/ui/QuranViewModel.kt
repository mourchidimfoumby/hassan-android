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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

class QuranViewModel(
    private val surahRepository: SurahRepository,
    private val surahVerseRepository: SurahVerseRepository,
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(QuranState())
    val uiState: StateFlow<QuranState> = _uiState.asStateFlow()

    init {
        initUiState()
    }

    private fun initUiState() {
        val surahsFlow = surahRepository.getSurahs()
        val allJuzFlow = surahVerseRepository.getAllJuz()
        val allHizbFlow = surahVerseRepository.getAllHizb()
        val preferencesFlow = surahVersePreferencesRepository.getSurahVersePreferencesFlow()

        combine(
            surahsFlow,
            allJuzFlow,
            allHizbFlow,
            preferencesFlow
        ) { surahs, allJuz, allHizb, preferences ->
            _uiState.update {
                it.copy(
                    surahs = surahs,
                    allJuz = allJuz,
                    allHizb = allHizb,
                    preferences = preferences,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    data class QuranState(
        val surahs: List<Surah> = emptyList(),
        val allJuz: List<Juz> = emptyList(),
        val allHizb: List<Hizb> = emptyList(),
        val preferences: SurahVersePreferences? = null,
        val isLoading: Boolean = true
    )
}