package com.mfoumby.hassan.quran.ui.reciters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecitersViewModel(
    private val reciterRepository: ReciterRepository,
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RecitersUiState())
    val uiState: StateFlow<RecitersUiState> = _uiState.asStateFlow()

    init {
        initReciters()
        listenSurahVersePreferences()
    }

    fun onReciterClick(reciter: Reciter) {
        val surahVersePreferences = uiState.value.surahVersePreferences ?: return
        val newReciter = if (surahVersePreferences.reciter?.id != reciter.id) reciter else null
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(
                surahVersePreferences.copy(reciter = newReciter)
            )
        }
    }

    private fun initReciters() {
        viewModelScope.launch {
            val reciters = reciterRepository.getReciters()
            _uiState.update {
                it.copy(reciters = reciters)
            }
            refreshInitializingState()
        }
    }

    private fun listenSurahVersePreferences() {
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

    data class RecitersUiState(
        val reciters: List<Reciter>? = null,
        val surahVersePreferences: SurahVersePreferences? = null,
        val initializing: Boolean = true
    ) {
        val initialized: Boolean
            get() = surahVersePreferences != null &&
                    reciters != null
    }
}