package com.mfoumby.hassan.quran

import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.common.domain.entity.Progress
import com.mfoumby.hassan.common.domain.extension.fromIndex
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerManifest
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseAudioRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseAudioFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixture2
import com.mfoumby.hassan.quran.domain.surahVerseFixtures
import com.mfoumby.hassan.quran.domain.surahVerseFixtures2
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.domain.surahVerseTranslationFixtures
import com.mfoumby.hassan.quran.ui.surahverse.SurahVerseViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class SurahVerseViewModelTest {
    private val surahRepository: SurahRepository = mockk()
    private val surahVerseRepository: SurahVerseRepository = mockk()
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository = mockk()
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository = mockk()
    private val surahVerseAudioRepository: SurahVerseAudioRepository = mockk()

    lateinit var viewModel: SurahVerseViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val quranModeFixture = QuranMode.SurahMode(surahVerseFixture.surah.number, null)

    @Before
    fun setUp() {
        coEvery { surahRepository.getSurah(any()) } returns surahFixture
        coEvery { surahVerseRepository.getSurahVersesFromSurah(any(), any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseRepository.getSurahVersesFromJuz(any(), any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseRepository.getSurahVersesFromHizb(any(), any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseRepository.getSurahVersesFromPage(any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseRepository.getSurahVerse(any(), any()) } returns surahVerseFixture
        coEvery { surahVerseTranslationRepository.getSurahVerseTranslations(any(), any()) } returns surahVerseTranslationFixtures
        every { surahVersePreferencesRepository.getSurahVersePreferencesFlow() } returns flowOf(surahVersePreferencesFixture)
        coEvery { surahVersePreferencesRepository.getSurahVersePreferences() } returns surahVersePreferencesFixture
        coEvery { surahVersePreferencesRepository.setSurahVersePreferences(any()) } returns Unit
        every { surahVerseAudioRepository.downloadSurahVerseAudio(any(), any()) } returns flow { emit(1); delay(100.milliseconds) }
        coEvery { surahVerseAudioRepository.getSurahVerseAudios(any(), any(), any(), any()) } returns listOf(surahVerseAudioFixture)
        coEvery { surahVerseAudioRepository.deleteSurahVerseAudios(any(), any()) } returns Unit
        coEvery { surahVerseAudioRepository.isSurahVerseAudioDownloaded(any(), any()) } returns false

        Dispatchers.setMain(testDispatcher)

        viewModel = SurahVerseViewModel(
            quranMode = quranModeFixture,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVerseAudioRepository = surahVerseAudioRepository
        )
    }

    @Test
    fun init_should_init_values() {
        // Given
        val surah = surahFixture
        val surahVerses = listOf(surahVerseFixture)
        val surahVerseTranslations = surahVerseTranslationFixtures
        val juz = surahVerses.first().verse.juz
        val hizb = surahVerses.first().verse.hizb
        val page = surahVerses.first().verse.page
        val surahVersePreferences = surahVersePreferencesFixture
        val informativeDisplayMode = SurahVerseViewModel.InformativeDisplayMode.ListMode(surahVerseFixture)
        val surahVersePlayerManifest = SurahVersePlayerManifest(
            reciter = surahVersePreferences.reciter!!,
            surahVerseAudios = listOf(surahVerseAudioFixture).associateBy { it.surah.number to it.verseNumber },
            state = SurahVersePlayerManifest.State.Idle
        )

        // Then
        assert(viewModel.uiState.value.surah == surah)
        assert(viewModel.uiState.value.surahVerses == surahVerses)
        assert(viewModel.uiState.value.translations == surahVerseTranslations)
        assert(viewModel.uiState.value.juz == juz)
        assert(viewModel.uiState.value.hizb == hizb)
        assert(viewModel.uiState.value.page == page)
        assert(viewModel.uiState.value.preferences == surahVersePreferences)
        assert(viewModel.uiState.value.informativeDisplayMode == informativeDisplayMode)
        assert(viewModel.uiState.value.playerManifest == surahVersePlayerManifest)
        assert(viewModel.uiState.value.audioDownloadProgress == null)
        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun onPageChange_should_update_ui_surah_data() = runTest {
        // Given
        val surahVerse = surahVerseFixture2
        when (quranModeFixture) {
            is QuranMode.SurahMode ->
                coEvery { surahVerseRepository.getSurahVersesFromSurah(any()) } returns listOf(surahVerse)

            is QuranMode.JuzMode ->
                coEvery { surahVerseRepository.getSurahVersesFromJuz(any()) } returns listOf(surahVerse)

            is QuranMode.HizbMode ->
                coEvery { surahVerseRepository.getSurahVersesFromHizb(any()) } returns listOf(surahVerse)
        }
        coEvery { surahVerseRepository.getSurahVersesFromPage(any()) } returns listOf(surahVerse)

        // When
        viewModel.onPageChange(surahVerse.verse.page)
        val results = mutableListOf<SurahVerseViewModel.SurahVerseUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(results)
        }
        val uiState = results.last()

        // Then
        assert(uiState.surah == surahVerse.surah)
        assert(uiState.surahVerses == listOf(surahVerse))
        assert(uiState.juz == surahVerse.verse.juz)
        assert(uiState.hizb == surahVerse.verse.hizb)
        assert(uiState.page == surahVerse.verse.page)
    }

    @Test
    fun onDisplayModeChange_should_update_ui_surah_data() = runTest {
        // Given
        val informativeDisplayMode = SurahVerseViewModel.InformativeDisplayMode.PageMode(surahVerseFixture2)
        coEvery { surahVerseRepository.getSurahVersesFromSurah(any()) } returns listOf(informativeDisplayMode.surahVerse)
        coEvery { surahVerseRepository.getSurahVersesFromPage(any()) } returns listOf(informativeDisplayMode.surahVerse)

        // When
        viewModel.onDisplayModeChange(informativeDisplayMode)
        val results = mutableListOf<SurahVerseViewModel.SurahVerseUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(results)
        }
        val uiState = results.last()

        // Then
        assert(uiState.surah == informativeDisplayMode.surahVerse.surah)
        assert(uiState.surahVerses == listOf(informativeDisplayMode.surahVerse))
        assert(uiState.page == informativeDisplayMode.surahVerse.verse.page)
        assert(uiState.informativeDisplayMode == informativeDisplayMode)
        coVerify {
            surahVersePreferencesRepository.setSurahVersePreferences(
                surahVersePreferencesFixture.copy(displayMode = informativeDisplayMode.toDisplayMode())
            )
        }
    }

    @Test
    fun onDisplayTranslationChange_should_update_surah_verse_preferences() {
        // Given
        val displayTranslation = true

        // When
        viewModel.onDisplayTranslationChange(displayTranslation)

        // Then
        coVerify {
            surahVersePreferencesRepository.setSurahVersePreferences(
                surahVersePreferencesFixture.copy(displayTranslation = displayTranslation)
            )
        }
    }

    @Test
    fun onDisplayTranslationChange_should_update_informative_display_mode() {
        // Given
        val displayTranslation = true

        // When
        viewModel.onDisplayTranslationChange(displayTranslation)

        // Then
        coVerify {
            surahVersePreferencesRepository.setSurahVersePreferences(
                surahVersePreferencesFixture.copy(displayTranslation = displayTranslation)
            )
        }
    }

    @Test
    fun downloadAudio_should_download_all_not_downloaded_surah_verse_audio() = runTest {
        // Given
        val surahVerses = (surahVerseFixtures + surahVerseFixtures2).associateBy { it.surah }
        val first = surahVerses.entries.first()
        val last = surahVerses.entries.last()

        coEvery { surahVerseRepository.getSurahVersesFromSurah(any()) } returns surahVerses.values.toList()
        coEvery { surahVerseAudioRepository.isSurahVerseAudioDownloaded(any(), any()) } returns false

        viewModel = SurahVerseViewModel(
            quranMode = quranModeFixture,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseAudioRepository = surahVerseAudioRepository
        )

        // When
        viewModel.downloadAudio()
        advanceUntilIdle()

        // Then
        coVerify {
            surahVerseAudioRepository.downloadSurahVerseAudio(first.value.surah, surahVersePreferencesFixture.reciter!!.id)
        }
        coVerify {
            surahVerseAudioRepository.downloadSurahVerseAudio(last.value.surah, surahVersePreferencesFixture.reciter!!.id)
        }
    }

    @Test
    fun downloadAudio_should_not_download_already_downloaded_surah_verse_audio() {
        // Given
        val surahVerses = (surahVerseFixtures + surahVerseFixtures2).associateBy { it.surah }
        val first = surahVerses.entries.first()
        val last = surahVerses.entries.last()

        coEvery { surahVerseRepository.getSurahVersesFromSurah(any()) } returns surahVerses.values.toList()
        coEvery { surahVerseAudioRepository.isSurahVerseAudioDownloaded(first.value.surah, any()) } returns true
        coEvery { surahVerseAudioRepository.isSurahVerseAudioDownloaded(last.value.surah, any()) } returns false

        viewModel = SurahVerseViewModel(
            quranMode = quranModeFixture,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseAudioRepository = surahVerseAudioRepository
        )

        // When
        viewModel.downloadAudio()

        // Then
        coVerify(exactly = 0) {
            surahVerseAudioRepository.downloadSurahVerseAudio(
                first.value.surah,
                surahVersePreferencesFixture.reciter!!.id
            )
        }
        coVerify {
            surahVerseAudioRepository.downloadSurahVerseAudio(
                last.value.surah,
                surahVersePreferencesFixture.reciter!!.id
            )
        }
    }

    @Test
    fun downloadAudio_should_update_downloading_progress_ui_state() = runTest {
        // Given
        val expectedResult = SurahVerseViewModel.AudioDownloadProgress(
            surah = surahVerseFixture.surah,
            reciter = surahVersePreferencesFixture.reciter!!,
            progress = Progress(1, surahVerseFixture.surah.totalVerses),
            currentStep = 1,
            totalSteps = 1
        )
        val results = mutableListOf<SurahVerseViewModel.SurahVerseUiState>()

        // When
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.drop(1).toList(results)
        }
        viewModel.downloadAudio()

        // Then
        assertEquals(expectedResult, results.first().audioDownloadProgress)
    }

    @Test
    fun downloadAudio_should_update_audio_ui_state() = runTest {
        // Given
        val surahVerseAudios = listOf(surahVerseAudioFixture)
        val expectedResult = surahVerseAudios.associateBy { it.surah.number to it.verseNumber }
        val results = mutableListOf<SurahVerseViewModel.SurahVerseUiState>()

        coEvery { surahVerseAudioRepository.getSurahVerseAudios(any(), any()) } returns surahVerseAudios

        viewModel = SurahVerseViewModel(
            quranMode = quranModeFixture,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseAudioRepository = surahVerseAudioRepository
        )

        // When
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(results)
        }
        viewModel.downloadAudio()
        advanceUntilIdle()

        // Then
        assertEquals(expectedResult, results.last().playerManifest?.surahVerseAudios)
        assertEquals(null, results.last().audioDownloadProgress)
    }

    @Test
    fun stopAudioDownload_should_stop_download_audio() {
        // When
        viewModel.stopAudioDownload()

        // Then
        assert(viewModel.audioDownloadJob == null)
    }

    @Test
    fun stopAudioDownload_should_delete_surah_verse_audio() {
        // When
        viewModel.stopAudioDownload()

        // Then
        coVerify {
            surahVerseAudioRepository.deleteSurahVerseAudios(surahFixture.number,
                surahVersePreferencesFixture.reciter!!.id
            )
        }
    }

    @Test
    fun onPlaySurahVerseAudio_should_emit_download_request_when_surah_verse_audios_are_not_downloaded() = runTest {
        // Given
        val surahVerseAudios = emptyList<SurahVerseAudio>()

        coEvery { surahVerseAudioRepository.getSurahVerseAudios(any(), any()) } returns surahVerseAudios

        viewModel = SurahVerseViewModel(
            quranMode = quranModeFixture,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseAudioRepository = surahVerseAudioRepository
        )

        // When
        val results = mutableListOf<SingleUiEvent?>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.event.toList(results)
        }
        viewModel.onPlaySurahVerseAudio(surahVerseFixture)

        // Then
        assert(results.first() is SurahVerseViewModel.SurahVerseUiEvent.DownloadAudioRequest)
    }

    @Test
    fun onPlaySurahVerseAudio_should_emit_error_when_surahVersePlayerData_is_null() = runTest {
        // Given
        coEvery { surahVersePreferencesRepository.getSurahVersePreferencesFlow() } returns emptyFlow()

        viewModel = SurahVerseViewModel(
            quranMode = quranModeFixture,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseAudioRepository = surahVerseAudioRepository
        )

        // When
        val results = mutableListOf<SingleUiEvent?>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.event.toList(results)
        }
        viewModel.onPlaySurahVerseAudio(surahVerseFixture)

        // Then
        assert(results.first() is SingleUiEvent.Error)
    }

    @Test
    fun onPlaySurahVerseAudio_should_play_surah_verse_audio_when_surah_verse_audios_are_downloaded() = runTest {
        // Given
        val surahVerses = surahVerseFixtures
        val surahVerseAudios = List(surahVerses.size) { surahVerseAudioFixture.copy(verseNumber = it.fromIndex()) }

        coEvery { surahVerseAudioRepository.getSurahVerseAudios(any(), any()) } returns surahVerseAudios
        coEvery { surahVerseRepository.getSurahVersesFromSurah(any(), any()) } returns surahVerses

        val expectedResult = SurahVersePlayerManifest.State.Playing(surahVerseAudios.first())

        viewModel = SurahVerseViewModel(
            quranMode = quranModeFixture,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseAudioRepository = surahVerseAudioRepository
        )

        // When
        viewModel.onPlaySurahVerseAudio(surahVerses.first())
        val result = viewModel.uiState.value.playerManifest

        // Then
        assert(result?.state == expectedResult)
    }

    @Test
    fun onAudioChange_should_update_surah_verse_player_data_state() {
        // Given
        val surahVerseAudio = surahVerseAudioFixture
        val expectedResult = SurahVersePlayerManifest.State.Playing(surahVerseAudio)

        coEvery { surahVerseAudioRepository.getSurahVerseAudios(any(), any()) } returns listOf(surahVerseAudio)

        // When
        viewModel.onAudioChange(1, 1)
        val result = viewModel.uiState.value.playerManifest

        // Then
        assert(result?.state == expectedResult)
    }

    @Test
    fun onSaveBookmark_should_save_bookmark() {
        // Given
        val surahVerse = surahVerseFixture2

        // When
        viewModel.onSaveBookmark(surahVerse)

        // Then
        coVerify {
            when (quranModeFixture) {
                is QuranMode.SurahMode -> {
                    surahVersePreferencesRepository.setSurahVersePreferences(
                        surahVersePreferencesFixture.copy(surahBookmark = surahVerse)
                    )
                }

                is QuranMode.JuzMode -> {
                    surahVersePreferencesRepository.setSurahVersePreferences(
                        surahVersePreferencesFixture.copy(juzBookmark = surahVerse)
                    )
                }

                is QuranMode.HizbMode -> {
                    surahVersePreferencesRepository.setSurahVersePreferences(
                        surahVersePreferencesFixture.copy(hizbBookmark = surahVerse)
                    )
                }
            }
        }
    }
}