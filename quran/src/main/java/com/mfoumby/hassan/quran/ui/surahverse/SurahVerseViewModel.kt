package com.mfoumby.hassan.quran.ui.surahverse

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.common.R
import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.common.domain.entity.Progress
import com.mfoumby.hassan.common.domain.extension.asIndex
import com.mfoumby.hassan.quran.QuranMode
import com.mfoumby.hassan.quran.domain.QuranUtils
import com.mfoumby.hassan.quran.domain.entity.ArabicTextFont
import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerManifest
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseAudioRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class SurahVerseViewModel(
    private val quranMode: QuranMode,
    private val surahRepository: SurahRepository,
    private val surahVerseRepository: SurahVerseRepository,
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository,
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository,
    private val surahVerseAudioRepository: SurahVerseAudioRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(SurahVerseUiState())
    val uiState: StateFlow<SurahVerseUiState> = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<SingleUiEvent?>()
    val event: SharedFlow<SingleUiEvent?> = _event.asSharedFlow()
    var audioDownloadJob: Job? = null
        private set

    init {
        initUiState()
    }

    fun onPageChange(page: Int) {
        viewModelScope.launch {
            setNewPage(page)
        }
    }

    fun onDisplayModeChange(informativeDisplayMode: InformativeDisplayMode) {
        val stateValue = uiState.value
        val preferences = stateValue.preferences ?: return
        if (preferences.displayMode == informativeDisplayMode.toDisplayMode()) return

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
                preferences.copy(displayMode = informativeDisplayMode.toDisplayMode())
            )
        }
    }

    fun onArabicTextFontChange(arabicTextFont: ArabicTextFont) {
        val preferences = uiState.value.preferences ?: return
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(preferences.copy(arabicTextFont = arabicTextFont))
        }
    }

    fun onIncreaseArabicTextFontSize() {
        val preferences = uiState.value.preferences ?: return
        val fontSize = preferences.arabicTextFontSize + 1
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(preferences.copy(arabicTextFontSize = fontSize))
        }
    }

    fun onDecreaseArabicTextFontSize() {
        val preferences = uiState.value.preferences ?: return
        val fontSize = (preferences.arabicTextFontSize - 1).takeIf { it >= 1 } ?: return
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(preferences.copy(arabicTextFontSize = fontSize))
        }
    }

    fun onDisplayTajweedChange(displayTajweed: Boolean) {
        val preferences = uiState.value.preferences ?: return
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(preferences.copy(displayTajweed = displayTajweed))
        }
    }

    fun onDisplayTranslationChange(displayTranslation: Boolean) {
        val preferences = uiState.value.preferences ?: return
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(preferences.copy(displayTranslation = displayTranslation))
        }
    }

    fun onAutomaticScrollingChange(audioAutomaticScrolling: Boolean) {
        val preferences = uiState.value.preferences ?: return
        viewModelScope.launch {
            surahVersePreferencesRepository.setSurahVersePreferences(preferences.copy(audioAutomaticScrolling = audioAutomaticScrolling))
        }
    }

    fun onPlaySurahVerseAudio(surahVerse: SurahVerse) {
        val stateValue = uiState.value
        viewModelScope.launch {
            try {
                when {
                    stateValue.playerManifest == null ->
                        _event.emit(SingleUiEvent.Error(R.string.none_reciter_selected_error))

                    stateValue.playerManifest.surahVerseAudios.size < stateValue.surahVerses.size ->
                        _event.emit(SurahVerseUiEvent.DownloadAudioRequest)

                    else -> {
                        updatePlayerState(surahVerse.surah.number, surahVerse.verse.verseNumber) { surahVerseAudio ->
                            _uiState.update { state ->
                                state.copy(currentAudioTrack = surahVerseAudio)
                            }
                            SurahVersePlayerManifest.State.Playing(surahVerseAudio)
                        }
                    }
                }
            } catch (_: Exception) {
                _event.emit(SingleUiEvent.Error(R.string.unknown_error))
            }
        }
    }

    fun onAudioChange(surahNumber: Int, verseNumber: Int) {
        updatePlayerState(surahNumber, verseNumber) { surahVerseAudio ->
            _uiState.update { state ->
                state.copy(currentAudioTrack = surahVerseAudio)
            }
            SurahVersePlayerManifest.State.Playing(surahVerseAudio)
        }
    }

    fun downloadAudio() {
        audioDownloadJob?.cancel()
        audioDownloadJob = null
        audioDownloadJob = viewModelScope.launch {
            try {
                val stateValue = requireNotNull(uiState.value)
                val reciter = requireNotNull(stateValue.preferences?.reciter)
                var currentStep = 0
                val groupedSurahVerses = stateValue.surahVerses.groupBy { it.surah }
                groupedSurahVerses
                    .filterNot {
                        surahVerseAudioRepository.isSurahVerseAudioDownloaded(it.value.first().surah, reciter.id)
                    }
                    .run {
                        val totalSteps = keys.size
                        forEach { (surah, _) ->
                            currentStep++
                            surahVerseAudioRepository.downloadSurahVerseAudio(surah, reciter.id).collect {
                                _uiState.update { state ->
                                    val audioDownloadProgress = AudioDownloadProgress(
                                        surah = surah,
                                        reciter = reciter,
                                        progress = Progress(it, surah.totalVerses),
                                        currentStep = currentStep,
                                        totalSteps = totalSteps
                                    )
                                    state.copy(audioDownloadProgress = audioDownloadProgress)
                                }
                            }
                        }
                    }

                val surahVerseAudios = groupedSurahVerses.flatMap { (surah, surahVerses) ->
                    val firstVerseNumber = surahVerses.first().verse.verseNumber
                    val lastVerseNumber = surahVerses.last().verse.verseNumber
                    val offset = firstVerseNumber - 1
                    val limit = lastVerseNumber - firstVerseNumber + 1
                    surahVerseAudioRepository.getSurahVerseAudios(surah, reciter.id, offset, limit)
                }

                _uiState.update { state ->
                    state.copy(
                        playerManifest = state.playerManifest?.copy(
                            surahVerseAudios = surahVerseAudios.associateBy { it.surah.number to it.verseNumber }
                        ),
                        audioDownloadProgress = null
                    )
                }
                _event.emit(SurahVerseUiEvent.AudioDownloadSuccess)
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
        val stateValue = uiState.value
        val reciter = stateValue.preferences?.reciter ?: return
        val surah = stateValue.surah ?: return
        viewModelScope.launch {
            surahVerseAudioRepository.deleteSurahVerseAudios(surah.number, reciter.id)
        }
    }

    fun onSaveBookmark(surahVerse: SurahVerse) {
        val preferences = uiState.value.preferences ?: return
        viewModelScope.launch {
            when (quranMode) {
                is QuranMode.SurahMode -> surahVersePreferencesRepository.setSurahVersePreferences(
                    preferences.copy(surahBookmark = surahVerse)
                )
                is QuranMode.JuzMode -> surahVersePreferencesRepository.setSurahVersePreferences(
                    preferences.copy(juzBookmark = surahVerse)
                )
                is QuranMode.HizbMode -> surahVersePreferencesRepository.setSurahVersePreferences(
                    preferences.copy(hizbBookmark = surahVerse)
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

    private fun updatePlayerState(
        surahNumber: Int,
        verseNumber: Int,
        newState: (SurahVerseAudio) -> SurahVersePlayerManifest.State
    ) {
        _uiState.update { state ->
            state.playerManifest?.surahVerseAudios
                ?.get(surahNumber to verseNumber)
                ?.let { surahVerseAudio ->
                    state.copy(
                        playerManifest = state.playerManifest.copy(
                            state = newState(surahVerseAudio)
                        )
                    )
                } ?: state
        }
    }

    private fun initUiState() {
        viewModelScope.launch {
            try {
                val surah = surahRepository.getSurah(quranMode.surahNumber) ?: throw Exception("Surah not found")
                val preferencesFlow = surahVersePreferencesRepository.getSurahVersePreferencesFlow()
                val preferences = preferencesFlow.first()
                val surahVerses = getSurahVerses(preferences)
                val targetSurahVerse = getTargetSurahVerse()
                val juz = getJuz(targetSurahVerse, surahVerses) ?: throw Exception("Juz not found")
                val hizb = getHizb(targetSurahVerse, surahVerses) ?: throw Exception("Hizb not found")
                val page = surahVerses.first().verse.page
                val informativeDisplayMode = getInformativeDisplayMode(preferences, targetSurahVerse, surahVerses) ?: throw Exception("Informative display mode not found")
                val translationsFlow = getSurahVerseTranslationsFlow(surah, juz, hizb)
                val playerManifestFlow = getSurahVersePlayerDataFlow()

                _uiState.update {
                    SurahVerseUiState(
                        surah = surah,
                        surahVerses = surahVerses,
                        juz = juz,
                        hizb = hizb,
                        page = page,
                        preferences = preferences,
                        informativeDisplayMode = informativeDisplayMode,
                        audioDownloadProgress = null,
                        isLoading = false
                    )
                }

                val preferencesJob = preferencesFlow.map { preferences ->
                    _uiState.update { state ->
                        state.copy(preferences = preferences)
                    }
                }.launchIn(viewModelScope)

                val translationsJob = translationsFlow.map { translations ->
                    _uiState.update { state ->
                        state.copy(translations = translations)
                    }
                }.launchIn(viewModelScope)

                val playerManifestJob = playerManifestFlow.map { playerManifest ->
                    _uiState.update { state ->
                        val playerState = state.playerManifest?.state ?: SurahVersePlayerManifest.State.Idle
                        state.copy(playerManifest = playerManifest.copy(state = playerState))
                    }
                }.launchIn(viewModelScope)

                listOf(
                    preferencesJob,
                    translationsJob,
                    playerManifestJob
                ).joinAll()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e)
                }
            }
        }
    }

    private suspend fun getSurahVerses(surahVersePreferences: SurahVersePreferences): List<SurahVerse> {
        var surahVerses = when (quranMode) {
            is QuranMode.SurahMode -> surahVerseRepository.getSurahVersesFromSurah(quranMode.surahNumber)
            is QuranMode.JuzMode -> surahVerseRepository.getSurahVersesFromJuz(quranMode.juzNumber)
            is QuranMode.HizbMode -> surahVerseRepository.getSurahVersesFromHizb(quranMode.hizbNumber)
        }
        val page = quranMode.verseNumber?.let {
            surahVerses.getOrNull(it.asIndex())?.verse?.page
        } ?: surahVerses.firstOrNull()?.verse?.page

        if (surahVersePreferences.displayMode == SurahVersePreferences.DisplayMode.PAGE) {
            page?.let { surahVerses = surahVerseRepository.getSurahVersesFromPage(it) }
        }

        return surahVerses
    }

    private suspend fun getTargetSurahVerse(): SurahVerse? {
        return quranMode.verseNumber?.let {
            surahVerseRepository.getSurahVerse(quranMode.surahNumber, it)
        }
    }

    private fun getJuz(targetSurahVerse: SurahVerse?, surahVerses: List<SurahVerse>): Int? {
        return targetSurahVerse?.verse?.juz ?: run {
            when (quranMode) {
                is QuranMode.SurahMode -> surahVerses.firstOrNull()?.verse?.juz
                is QuranMode.JuzMode -> quranMode.juzNumber
                is QuranMode.HizbMode -> QuranUtils.calculateJuz(quranMode.hizbNumber)
            }
        }
    }

    private fun getHizb(targetSurahVerse: SurahVerse?, surahVerses: List<SurahVerse>): Int? {
        return targetSurahVerse?.verse?.hizb ?: run {
            when (quranMode) {
                is QuranMode.SurahMode -> surahVerses.firstOrNull()?.verse?.hizb
                is QuranMode.JuzMode -> QuranUtils.calculateHizb(quranMode.juzNumber)
                is QuranMode.HizbMode -> quranMode.hizbNumber
            }
        }
    }

    private fun getInformativeDisplayMode(
        preferences: SurahVersePreferences,
        targetSurahVerse: SurahVerse?,
        surahVerses: List<SurahVerse>
    ): InformativeDisplayMode? {
        return (targetSurahVerse ?: run { surahVerses.firstOrNull() })?.let {
            InformativeDisplayMode.fromDisplayMode(preferences.displayMode, it)
        }
    }

    private fun getSurahVerseTranslationsFlow(surah: Surah, juz: Int, hizb: Int): Flow<List<SurahVerseTranslation>> {
        val translationLanguageFlow = surahVersePreferencesRepository.getSurahVersePreferencesFlow()
            .map { it.translationLanguage }
            .distinctUntilChanged()

        val surahFlow = uiState
            .mapNotNull { it.surah }
            .onStart { emit(surah) }
            .distinctUntilChanged()

        val juzFlow = uiState
            .mapNotNull { it.juz }
            .onStart { emit(juz) }
            .distinctUntilChanged()

        val hizbFlow = uiState
            .mapNotNull { it.hizb }
            .onStart { emit(hizb) }
            .distinctUntilChanged()

        return combine(
            translationLanguageFlow,
            surahFlow,
            juzFlow,
            hizbFlow
        ) { language, surah, juz, hizb ->
            if (language == null) return@combine emptyList()
            when (quranMode) {
                is QuranMode.SurahMode -> surahVerseTranslationRepository.getSurahVerseTranslations(surah.number, language)
                is QuranMode.JuzMode -> surahVerseTranslationRepository.getSurahVerseTranslationsFromJuz(juz, language)
                is QuranMode.HizbMode -> surahVerseTranslationRepository.getSurahVerseTranslationsFromHizb(hizb, language)
            }
        }
    }

    private fun getSurahVersePlayerDataFlow() : Flow<SurahVersePlayerManifest> {
        val reciterFlow = surahVersePreferencesRepository.getSurahVersePreferencesFlow()
            .mapNotNull { it.reciter }
            .distinctUntilChanged()

        val surahVersesFlow = uiState
            .map { it.surahVerses }
            .filterNot { it.isEmpty() }
            .distinctUntilChangedBy { surahVerses ->
                when (quranMode) {
                    is QuranMode.SurahMode -> surahVerses.first().surah
                    is QuranMode.JuzMode -> surahVerses.first().verse.juz
                    is QuranMode.HizbMode -> surahVerses.first().verse.hizb
                }
            }

        return combine(reciterFlow, surahVersesFlow) { reciter, surahVerses ->
            reciter to surahVerses
        }.mapLatest { (reciter, surahVerses) ->
            val surahVerseAudios: List<SurahVerseAudio> = when (quranMode) {
                is QuranMode.SurahMode -> {
                    val surah = surahVerses.first().surah
                    surahVerseAudioRepository.getSurahVerseAudios(surah, reciter.id)
                }

                is QuranMode.JuzMode, is QuranMode.HizbMode -> {
                    surahVerses
                        .groupBy { it.surah }
                        .flatMap { (surah, verses) ->
                            val firstVerseNumber = verses.first().verse.verseNumber
                            val lastVerseNumber = verses.last().verse.verseNumber
                            val offset = firstVerseNumber - 1
                            val limit = lastVerseNumber - firstVerseNumber + 1
                            surahVerseAudioRepository.getSurahVerseAudios(surah, reciter.id, offset, limit)
                        }
                }
            }

            SurahVersePlayerManifest(
                reciter = reciter,
                surahVerseAudios = surahVerseAudios.associateBy { it.surah.number to it.verseNumber },
                state = SurahVersePlayerManifest.State.Idle
            )
        }
    }

    data class SurahVerseUiState(
        val surah: Surah? = null,
        val surahVerses: List<SurahVerse> = emptyList(),
        val translations: List<SurahVerseTranslation> = emptyList(),
        val juz: Int? = null,
        val hizb: Int? = null,
        val page: Int? = null,
        val preferences: SurahVersePreferences? = null,
        val informativeDisplayMode: InformativeDisplayMode? = null,
        val playerManifest: SurahVersePlayerManifest? = null,
        val currentAudioTrack: SurahVerseAudio? = null,
        val audioDownloadProgress: AudioDownloadProgress? = null,
        val isLoading: Boolean = true,
        val error: Throwable? = null
    )

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

    data class AudioDownloadProgress(
        val surah: Surah,
        val reciter: Reciter,
        val progress: Progress,
        val currentStep: Int,
        val totalSteps: Int
    )

    sealed interface SurahVerseUiEvent: SingleUiEvent {
        data object DownloadAudioRequest: SurahVerseUiEvent
        data object AudioDownloadSuccess: SurahVerseUiEvent
        data class AudioDownloadError(@param:StringRes val messageId: Int): SurahVerseUiEvent
    }
}