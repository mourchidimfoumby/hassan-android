package com.mfoumby.hassan.quran.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuranViewModel(
    private val surahRepository: SurahRepository,
    private val surahVerseRepository: SurahVerseRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(QuranState())
    val uiState: StateFlow<QuranState> = _uiState.asStateFlow()

    init {
        initSurahs()
        initAllJuz()
    }

    fun onTabChange(tab: QuranTab) {
        _uiState.update {
            it.copy(tab = tab)
        }
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

    private fun refreshInitializingState() {
        if (!uiState.value.initializing) return
        _uiState.update {
            it.copy(initializing = !it.initialized)
        }
    }

    data class QuranState(
        val surahs: List<Surah>? = null,
        val allJuz: List<Juz>? = null,
        val tab: QuranTab = QuranTab.SURAH,
        val initializing: Boolean = true
    ) {
        val initialized: Boolean
            get() = surahs != null &&
                    allJuz != null
    }

    enum class QuranTab {
        SURAH,
        JUZ
    }
}