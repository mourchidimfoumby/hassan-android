package com.mfoumby.hassan.quran.ui.surahverse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.common.R
import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.common.domain.entity.Progress
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerData
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.usecase.GetSurahVerseFlowUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

class SurahVerseViewModel(
    private val surahNumber: Int,
    private val surahRepository: SurahRepository,
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository,
    private val reciterRepository: ReciterRepository,
    private val getSurahVerseFlowUseCase: GetSurahVerseFlowUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SurahVerseUiState())
    val uiState: StateFlow<SurahVerseUiState> = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<SingleUiEvent?>()
    val event: SharedFlow<SingleUiEvent?> = _event.asSharedFlow()
    var audioDownloadJob: Job? = null
        private set

    init {
        initSurah()
        initSurahVerses()
        initSurahVersePreferences()
        initSurahVersePlayerData()
    }

    fun onDisplayTranslationChange(displayTranslation: Boolean) {
        val surahPreferences = uiState.value.surahVersePreferences ?: return
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(surahPreferences.copy(displayTranslation = displayTranslation))
        }
    }

    fun downloadAudio() {
        audioDownloadJob?.cancel()
        audioDownloadJob = null
        audioDownloadJob = viewModelScope.launch {
            try {
                val state = uiState.value
                val surah = requireNotNull(state.surah)
                val surahVerseAudios = requireNotNull(state.surahVersePlayerData?.surahVerseAudios)
                val reciter = requireNotNull(state.surahVersePreferences?.reciter)

                if (surahVerseAudios.size < surah.totalVerses) {
                    reciterRepository.downloadSurahVerseAudio(surah, reciter.id).collect {
                        _uiState.update { state ->
                            state.copy(audioDownloadProgress = Progress(it, surah.totalVerses))
                        }
                    }
                    _event.emit(SurahVerseUiEvent.AudioDownloadSuccess)
                    _uiState.update {
                        it.copy(audioDownloadProgress = null)
                    }
                }
            } catch (_: UnknownHostException) {
                _event.emit(SurahVerseUiEvent.AudioDownloadError(R.string.not_internet_connection_error))
            } catch (_: IOException) {
                _event.emit(SurahVerseUiEvent.AudioDownloadError(R.string.any_network_error))
            } catch (_: Exception) {
                _event.emit(SurahVerseUiEvent.AudioDownloadError(R.string.unknown_error))
            }
        }
    }

    fun stopAudioDownload() {
        audioDownloadJob?.cancel()
        audioDownloadJob = null
        _uiState.update {
            it.copy(audioDownloadProgress = null)
        }
        val reciter = uiState.value.surahVersePreferences?.reciter ?: return
        viewModelScope.launch {
            reciterRepository.deleteSurahVerseAudios(surahNumber, reciter.id)
        }
    }

    fun refreshSurahVerseAudios() {
        val reciter = uiState.value.surahVersePreferences?.reciter ?: return
        viewModelScope.launch {
            val surahVerseAudios = reciterRepository.getSurahVerseAudios(surahNumber, reciter.id)
            _uiState.update { state ->
                state.copy(
                    surahVersePlayerData = state.surahVersePlayerData?.copy(
                        surahVerseAudios = surahVerseAudios
                    )
                )
            }
        }
    }

    fun onPlayAudio(verseNumber: Int) {
        viewModelScope.launch {
            try {
                val state = uiState.value
                val surah = requireNotNull(state.surah)
                val surahPlayerData = state.surahVersePlayerData

                when {
                    surahPlayerData == null ->
                        _event.emit(SingleUiEvent.Error(R.string.none_reciter_selected_error))

                    surahPlayerData.surahVerseAudios.size < surah.totalVerses ->
                        _event.emit(SurahVerseUiEvent.DownloadAudioRequest)

                    else -> playAudio(verseNumber)
                }
            } catch (_: Exception) {
                _event.emit(SingleUiEvent.Error(R.string.unknown_error))
            }
        }
    }

    fun onAudioChange(verseNumber: Int) {
        updateSurahVersePlayerDataState(verseNumber - 1) {
            SurahVersePlayerData.State.Playing(it)
        }
    }

    private fun playAudio(verseNumber: Int) {
        updateSurahVersePlayerDataState(verseNumber - 1) {
            SurahVersePlayerData.State.Playing(it)
        }
    }

    private fun updateSurahVersePlayerDataState(
        surahVerseAudioIndex: Int,
        stateFactory: (SurahVerseAudio) -> SurahVersePlayerData.State
    ) {
        _uiState.update { state ->
            state.surahVersePlayerData?.surahVerseAudios?.getOrNull(surahVerseAudioIndex)?.let { surahVerseAudio ->
                state.copy(
                    surahVersePlayerData = state.surahVersePlayerData.copy(
                        state = stateFactory(surahVerseAudio)
                    )
                )
            } ?: state
        }
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
            surahVersePreferencesRepository.getSurahVersePreferencesFlow().collect { surahVersePreferences ->
                _uiState.update {
                    it.copy(surahVersePreferences = surahVersePreferences)
                }
                refreshInitializingState()
            }
        }
    }

    private fun initSurahVersePlayerData() {
        viewModelScope.launch {
            surahVersePreferencesRepository.getSurahVersePreferencesFlow()
                .distinctUntilChangedBy { it.reciter }
                .mapNotNull { it.reciter }
                .collect{ reciter ->
                    val surahVerseAudios = reciterRepository.getSurahVerseAudios(surahNumber, reciter.id)
                    val surahVersePlayerData = SurahVersePlayerData(
                        reciter = reciter,
                        surahVerseAudios = surahVerseAudios,
                        state = SurahVersePlayerData.State.Idle
                    )

                    _uiState.update {
                        it.copy(surahVersePlayerData = surahVersePlayerData)
                    }
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
        val surahVersePlayerData: SurahVersePlayerData? = null,
        val surahVersePreferences: SurahVersePreferences? = null,
        val audioDownloadProgress: Progress? = null,
        val initializing: Boolean = true
    ) {
        val initialized: Boolean
            get() = surah != null &&
                    surahVerses.isNotEmpty() &&
                    surahVersePreferences != null
    }

    sealed interface SurahVerseUiEvent: SingleUiEvent {
        data object DownloadAudioRequest: SurahVerseUiEvent
        data object AudioDownloadSuccess: SurahVerseUiEvent
        data class AudioDownloadError(val messageId: Int): SurahVerseUiEvent
    }
}