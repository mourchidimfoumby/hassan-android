package com.mfoumby.hassan.quran.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class QuranViewModel(
    private val surahRepository: SurahRepository
): ViewModel() {
    val uiState: StateFlow<QuranState> = initUiState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = QuranState()
    )

    private fun initUiState(): Flow<QuranState> = combine(surahRepository.getSurahs()) { (surahs) ->
            QuranState(surahs = surahs)
        }

    data class QuranState(val surahs: List<Surah>? = null)
}