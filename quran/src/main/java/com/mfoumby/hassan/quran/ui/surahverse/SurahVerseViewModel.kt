package com.mfoumby.hassan.quran.ui.surahverse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.common.R
import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.common.domain.entity.Progress
import com.mfoumby.hassan.common.domain.extension.asIndex
import com.mfoumby.hassan.quran.QuranMode
import com.mfoumby.hassan.quran.domain.QuranUtils
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerData
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

class SurahVerseViewModel(
    private val quranMode: QuranMode,
    private val surahRepository: SurahRepository,
    private val surahVerseRepository: SurahVerseRepository,
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository,
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository,
    private val reciterRepository: ReciterRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SurahVerseUiState())
    val uiState: StateFlow<SurahVerseUiState> = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<SingleUiEvent?>()
    val event: SharedFlow<SingleUiEvent?> = _event.asSharedFlow()
    var audioDownloadJob: Job? = null
        private set

    init {
        initSurah()
        initJuzNumber()
        initHizbNumber()
        initSurahVerses()
        initSurahVerseTranslations()
        initSurahVersePreferences()
        initInformativeDisplayMode()
        initSurahVersePlayerData()
    }

    fun onPageChange(page: Int) {
        viewModelScope.launch {
            setNewPage(page)
        }
    }

    fun onDisplayModeChange(informativeDisplayMode: InformativeDisplayMode) {
        val stateValue = uiState.value
        val surahVersePreferences = stateValue.surahVersePreferences ?: return
        if (surahVersePreferences.displayMode == informativeDisplayMode.toDisplayMode()) return

        viewModelScope.launch {
            val surahVerses = when (informativeDisplayMode) {
                is InformativeDisplayMode.ListMode -> {
                    when (quranMode) {
                        is QuranMode.SurahMode -> surahVerseRepository.getSurahVersesFromSurah(informativeDisplayMode.surahVerse.surah.number)
                        is QuranMode.JuzMode -> surahVerseRepository.getSurahVersesFromJuz(informativeDisplayMode.surahVerse.verse.juz)
                        is QuranMode.HizbMode -> surahVerseRepository.getSurahVersesFromHizb(informativeDisplayMode.surahVerse.verse.hizb)
                    }
                }

                is InformativeDisplayMode.PageMode -> surahVerseRepository.getSurahVersesFromPage(informativeDisplayMode.surahVerse.verse.page)
            }

            _uiState.update {
                it.copy(
                    surah = informativeDisplayMode.surahVerse.surah,
                    surahVerses = surahVerses,
                    page = informativeDisplayMode.surahVerse.verse.page,
                    juz = informativeDisplayMode.surahVerse.verse.juz,
                    hizb = informativeDisplayMode.surahVerse.verse.hizb,
                    informativeDisplayMode = informativeDisplayMode
                )
            }

            surahVersePreferencesRepository.setSurahVersePreferences(
                surahVersePreferences.copy(displayMode = informativeDisplayMode.toDisplayMode())
            )
        }
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
                    val downloadedSurahVerseAudios = reciterRepository.getSurahVerseAudios(surah.number, reciter.id)
                    _uiState.update { state ->
                        state.copy(
                            surahVersePlayerData = state.surahVersePlayerData?.copy(
                                surahVerseAudios = downloadedSurahVerseAudios
                            ),
                            audioDownloadProgress = null
                        )
                    }
                    _event.emit(SurahVerseUiEvent.AudioDownloadSuccess)
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
        val surah = uiState.value.surah ?: return
        viewModelScope.launch {
            reciterRepository.deleteSurahVerseAudios(surah.number, reciter.id)
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
        updateSurahVersePlayerDataState(verseNumber.asIndex()) {
            SurahVersePlayerData.State.Playing(it)
        }
    }

    fun onSaveBookmark(surahVerse: SurahVerse) {
        val surahVersePreferences = uiState.value.surahVersePreferences ?: return
        viewModelScope.launch {
            when (quranMode) {
                is QuranMode.SurahMode -> surahVersePreferencesRepository.setSurahVersePreferences(
                    surahVersePreferences.copy(surahBookmark = surahVerse)
                )
                is QuranMode.JuzMode -> surahVersePreferencesRepository.setSurahVersePreferences(
                    surahVersePreferences.copy(juzBookmark = surahVerse)
                )
                is QuranMode.HizbMode -> surahVersePreferencesRepository.setSurahVersePreferences(
                    surahVersePreferences.copy(hizbBookmark = surahVerse)
                )
            }
        }
    }

    private suspend fun setNewPage(page: Int) {
        val informativeDisplayMode = uiState.value.informativeDisplayMode ?: return
        val surahVerses = when(informativeDisplayMode) {
            is InformativeDisplayMode.ListMode -> {
                when (quranMode) {
                    is QuranMode.SurahMode -> surahVerseRepository.getSurahVersesFromSurah(page)
                    is QuranMode.JuzMode -> surahVerseRepository.getSurahVersesFromJuz(page)
                    is QuranMode.HizbMode -> surahVerseRepository.getSurahVersesFromHizb(page)
                }
            }
            is InformativeDisplayMode.PageMode -> surahVerseRepository.getSurahVersesFromPage(page)
        }
        val fistVerse = surahVerses.firstOrNull() ?: return
        _uiState.update {
            it.copy(
                surah = fistVerse.surah,
                surahVerses = surahVerses,
                juz = fistVerse.verse.juz,
                hizb = fistVerse.verse.hizb,
                page = fistVerse.verse.page
            )
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
            val surah = surahRepository.getSurah(quranMode.surahNumber)
            _uiState.update {
                it.copy(surah = surah)
            }
            refreshInitializingState()
        }
    }

    private fun initJuzNumber() {
        viewModelScope.launch {
            val juz = when (quranMode) {
                is QuranMode.SurahMode -> {
                    quranMode.verseNumber?.let {
                        surahVerseRepository.getSurahVerse(quranMode.surahNumber, it)?.verse?.juz
                    } ?: surahVerseRepository.getSurahVersesFromSurah(quranMode.surahNumber, 1).first().verse.juz
                }
                is QuranMode.JuzMode -> quranMode.juzNumber
                is QuranMode.HizbMode -> QuranUtils.calculateJuz(quranMode.hizbNumber)
            }
            _uiState.update {
                it.copy(juz = juz)
            }
            refreshInitializingState()
        }
    }

    private fun initHizbNumber() {
        viewModelScope.launch {
            val hizb = quranMode.verseNumber?.let {
                surahVerseRepository.getSurahVerse(quranMode.surahNumber, it)?.verse?.hizb
            } ?: run {
                when (quranMode) {
                    is QuranMode.SurahMode -> surahVerseRepository.getSurahVersesFromSurah(quranMode.surahNumber, 1).first().verse.hizb
                    is QuranMode.JuzMode -> QuranUtils.calculateHizb(quranMode.juzNumber)
                    is QuranMode.HizbMode -> quranMode.hizbNumber
                }
            }
            _uiState.update {
                it.copy(hizb = hizb)
            }
            refreshInitializingState()
        }
    }

    private fun initSurahVerses() {
        viewModelScope.launch {
            val displayMode = surahVersePreferencesRepository.getSurahVersePreferences()?.displayMode
            var surahVerses = when (quranMode) {
                is QuranMode.SurahMode -> surahVerseRepository.getSurahVersesFromSurah(quranMode.surahNumber)
                is QuranMode.JuzMode -> surahVerseRepository.getSurahVersesFromJuz(quranMode.juzNumber)
                is QuranMode.HizbMode -> surahVerseRepository.getSurahVersesFromHizb(quranMode.hizbNumber)
            }
            val page = quranMode.verseNumber?.let {
                surahVerses.getOrNull(it.asIndex())?.verse?.page
            } ?: surahVerses.first().verse.page

            if (displayMode == SurahVersePreferences.DisplayMode.PAGE) {
                surahVerses = surahVerseRepository.getSurahVersesFromPage(page)
            }

            _uiState.update { state ->
                state.copy(
                    surahVerses = surahVerses,
                    page = page
                )
            }
            refreshInitializingState()
        }
    }

    private fun initSurahVerseTranslations() {
        combine(
            surahVersePreferencesRepository.getSurahVersePreferencesFlow()
                .mapNotNull { it.translationLanguage }
                .distinctUntilChanged(),
            uiState.mapNotNull { it.surah }.distinctUntilChanged(),
            uiState.map { it.juz }.filterNot { it == -1 }.distinctUntilChanged(),
            uiState.map { it.hizb }.filterNot { it == -1 }.distinctUntilChanged()
        ) { language, surah, juz, hizb ->
            val surahVerseTranslations = when (quranMode) {
                is QuranMode.SurahMode -> surahVerseTranslationRepository.getSurahVerseTranslations(surah.number, language)
                is QuranMode.JuzMode -> surahVerseTranslationRepository.getSurahVerseTranslationsFromJuz(juz, language)
                is QuranMode.HizbMode -> surahVerseTranslationRepository.getSurahVerseTranslationsFromHizb(hizb, language)
            }
            _uiState.update {
                it.copy(surahVerseTranslations = surahVerseTranslations)
            }
        }.launchIn(viewModelScope)
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

    private fun initInformativeDisplayMode() {
        viewModelScope.launch {
            val displayMode = surahVersePreferencesRepository.getSurahVersePreferences()?.displayMode ?: SurahVersePreferences.DisplayMode.LIST
            val surahVerse = quranMode.verseNumber?.let {
                surahVerseRepository.getSurahVerse(quranMode.surahNumber, it)
            } ?: run {
                when (quranMode) {
                    is QuranMode.SurahMode -> surahVerseRepository.getSurahVersesFromSurah(quranMode.surahNumber)
                    is QuranMode.JuzMode -> surahVerseRepository.getSurahVersesFromJuz(quranMode.juzNumber)
                    is QuranMode.HizbMode -> surahVerseRepository.getSurahVersesFromHizb(quranMode.hizbNumber)
                }.first()
            }
            _uiState.update {
                it.copy(informativeDisplayMode = InformativeDisplayMode.fromDisplayMode(displayMode, surahVerse))
            }
            refreshInitializingState()
        }
    }

    private fun initSurahVersePlayerData() {
        viewModelScope.launch {
            surahVersePreferencesRepository.getSurahVersePreferencesFlow()
                .distinctUntilChangedBy { it.reciter }
                .mapNotNull { it.reciter }
                .collect{ reciter ->
                    val surahVerseAudios = reciterRepository.getSurahVerseAudios(quranMode.surahNumber, reciter.id)
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
        val surahVerseTranslations: List<SurahVerseTranslation> = emptyList(),
        val juz: Int = -1,
        val hizb: Int = -1,
        val page: Int = -1,
        val surahVersePreferences: SurahVersePreferences? = null,
        val informativeDisplayMode: InformativeDisplayMode? = null,
        val surahVersePlayerData: SurahVersePlayerData? = null,
        val audioDownloadProgress: Progress? = null,
        val initializing: Boolean = true
    ) {
        val initialized: Boolean
            get() = surah != null &&
                    surahVerses.isNotEmpty() &&
                    juz != -1 &&
                    hizb != -1 &&
                    page != -1 &&
                    surahVersePreferences != null &&
                    informativeDisplayMode != null
    }

    sealed class InformativeDisplayMode(open val surahVerse: SurahVerse) {
        data class ListMode(override val surahVerse: SurahVerse): InformativeDisplayMode(surahVerse)
        data class PageMode(override val surahVerse: SurahVerse): InformativeDisplayMode(surahVerse)

        fun toDisplayMode(): SurahVersePreferences.DisplayMode = when (this) {
            is ListMode -> SurahVersePreferences.DisplayMode.LIST
            is PageMode -> SurahVersePreferences.DisplayMode.PAGE
        }

        companion object {
            fun fromDisplayMode(
                displayMode: SurahVersePreferences.DisplayMode,
                surahVerse: SurahVerse
            ): InformativeDisplayMode = when (displayMode) {
                SurahVersePreferences.DisplayMode.LIST -> ListMode(surahVerse)
                SurahVersePreferences.DisplayMode.PAGE -> PageMode(surahVerse)
            }
        }
    }

    sealed interface SurahVerseUiEvent: SingleUiEvent {
        data object DownloadAudioRequest: SurahVerseUiEvent
        data object AudioDownloadSuccess: SurahVerseUiEvent
        data class AudioDownloadError(val messageId: Int): SurahVerseUiEvent
    }
}