package com.mfoumby.hassan.quran

import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerData
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseAudioFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixture2
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.ui.surahverse.SurahVerseViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SurahVerseViewModelTest {
    private val surahRepository: SurahRepository = mockk()
    private val surahVerseRepository: SurahVerseRepository = mockk()
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository = mockk()
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository = mockk()
    private val reciterRepository: ReciterRepository = mockk()

    lateinit var viewModel: SurahVerseViewModel
    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setUp() {
        coEvery { surahRepository.getSurah(any()) } returns surahFixture
        coEvery { surahVerseRepository.getSurahVersesFromPage(any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseRepository.getSurahVersesFromSurah(any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseTranslationRepository.getSurahVerseTranslations(any(), any()) } returns listOf()
        every { surahVersePreferencesRepository.getSurahVersePreferencesFlow() } returns flowOf(surahVersePreferencesFixture)
        coEvery { surahVersePreferencesRepository.getSurahVersePreferences() } returns surahVersePreferencesFixture
        coEvery { surahVersePreferencesRepository.setSurahVersePreferences(any()) } returns Unit
        every { reciterRepository.downloadSurahVerseAudio(any(), any()) } returns flowOf(1)
        coEvery { reciterRepository.getSurahVerseAudios(any(), any()) } returns listOf(surahVerseAudioFixture)
        coEvery { reciterRepository.deleteSurahVerseAudios(any(), any()) } returns Unit

        Dispatchers.setMain(testDispatcher)

        viewModel = SurahVerseViewModel(
            surahNumber = 1,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            reciterRepository = reciterRepository
        )
    }

    @Test
    fun init_should_init_values() {
        // Given
        val surah = surahFixture
        val surahVerses = listOf(surahVerseFixture)
        val surahVersePreferences = surahVersePreferencesFixture
        val surahVersePlayerData = SurahVersePlayerData(
            reciter = surahVersePreferences.reciter!!,
            surahVerseAudios = listOf(surahVerseAudioFixture),
            state = SurahVersePlayerData.State.Idle
        )
        val informativeDisplayMode = SurahVerseViewModel.InformativeDisplayMode.ListMode(surahVerseFixture)

        // Then
        assert(viewModel.uiState.value.surah == surah)
        assert(viewModel.uiState.value.surahVerses == surahVerses)
        assert(viewModel.uiState.value.surahVersePreferences == surahVersePreferences)
        assert(viewModel.uiState.value.surahVersePlayerData == surahVersePlayerData)
        assert(viewModel.uiState.value.informativeDisplayMode == informativeDisplayMode)
        assert(!viewModel.uiState.value.initializing)
    }

    @Test
    fun onSurahChange_should_update_ui_surah_data() = runTest {
        // Given
        val surahVerse = surahVerseFixture2
        coEvery { surahVerseRepository.getSurahVersesFromSurah(any()) } returns listOf(surahVerse)

        // When
        viewModel.onSurahChange(surahVerse.surah.number)
        val results = mutableListOf<SurahVerseViewModel.SurahVerseUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.toList(results)
        }
        val uiState = results.last()

        // Then
        assert(uiState.surah == surahVerse.surah)
        assert(uiState.surahVerses == listOf(surahVerse))
        assert(uiState.page == surahVerse.verse.page)
    }

    @Test
    fun onPageChange_should_update_ui_surah_data() = runTest {
        // Given
        val surahVerse = surahVerseFixture2
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
    fun downloadAudio_should_download_surah_verse_audio() {
        // When
        viewModel.downloadAudio()

        // Then
        coVerify {
            reciterRepository.downloadSurahVerseAudio(surahFixture, surahVersePreferencesFixture.reciter!!.id)
        }
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
            reciterRepository.deleteSurahVerseAudios(surahFixture.number,
                surahVersePreferencesFixture.reciter!!.id
            )
        }
    }

    @Test
    fun onPlayAudio_should_emit_download_request_when_surah_verse_audios_are_not_downloaded() = runTest {
        // Given
        coEvery { reciterRepository.getSurahVerseAudios(any(), any()) } returns emptyList()
        viewModel = SurahVerseViewModel(
            surahNumber = 1,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            reciterRepository = reciterRepository
        )

        // When
        val results = mutableListOf<SingleUiEvent?>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.event.toList(results)
        }
        viewModel.onPlayAudio(1)

        // Then
        assert(results.first() is SurahVerseViewModel.SurahVerseUiEvent.DownloadAudioRequest)
    }

    @Test
    fun onPlayAudio_should_play_audio_when_surah_verse_audios_are_downloaded() {
        // Given
        val surahVerseAudios = List(surahFixture.totalVerses) { surahVerseAudioFixture }
        coEvery { reciterRepository.getSurahVerseAudios(any(), any()) } returns surahVerseAudios
        val expectedResult = SurahVersePlayerData.State.Playing(surahVerseAudios.first())
        viewModel = SurahVerseViewModel(
            surahNumber = 1,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            reciterRepository = reciterRepository
        )

        // When
        viewModel.onPlayAudio(1)
        val result = viewModel.uiState.value.surahVersePlayerData

        // Then
        assert(result?.state == expectedResult)
    }

    @Test
    fun onAudioChange_should_update_surah_verse_player_data_state() {
        // Given
        val verseNumber = 1
        val expectedResult = SurahVersePlayerData.State.Playing(surahVerseAudioFixture)

        // When
        viewModel.onAudioChange(verseNumber)
        val result = viewModel.uiState.value.surahVersePlayerData

        // Then
        assert(result?.state == expectedResult)
    }
}