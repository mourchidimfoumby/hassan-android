package com.mfoumby.hassan.quran.ui.surahverse.reciters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class RecitersViewModel(
    private val reciterRepository: ReciterRepository,
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RecitersUiState())
    val uiState: StateFlow<RecitersUiState> = _uiState.asStateFlow()

    init {
        initUiState()
    }

    fun onReciterClick(reciter: Reciter) {
        val preferences = uiState.value.preferences ?: return
        val newReciter = if (preferences.reciter?.id != reciter.id) reciter else null
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(
                preferences.copy(reciter = newReciter)
            )
        }
    }

    private fun initUiState() {
        viewModelScope.launch {
            val reciters = reciterRepository.getReciters()
            val preferencesFlow = surahVersePreferencesRepository.getSurahVersePreferencesFlow()

            _uiState.update {
                it.copy(
                    reciters = reciters,
                    preferences = preferencesFlow.first(),
                    isLoading = false
                )
            }

            val preferencesJob = preferencesFlow.map {
                _uiState.update { state ->
                    state.copy(preferences = it)
                }
            }.launchIn(viewModelScope)

            listOf(preferencesJob).joinAll()
        }
    }

    data class RecitersUiState(
        val reciters: List<Reciter>? = null,
        val preferences: SurahVersePreferences? = null,
        val isLoading: Boolean = true
    )
}