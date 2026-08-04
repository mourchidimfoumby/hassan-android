package com.mfoumby.hassan.quran

import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.quran.domain.entity.SurahVersePlayerData
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseAudioRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import com.mfoumby.hassan.quran.domain.surahFixture
import com.mfoumby.hassan.quran.domain.surahVerseAudioFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixture
import com.mfoumby.hassan.quran.domain.surahVerseFixture2
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.domain.surahVerseTranslationFixtures
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
    private val surahVerseAudioRepository: SurahVerseAudioRepository = mockk()

    lateinit var viewModel: SurahVerseViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val quranModeFixture = QuranMode.SurahMode(surahVerseFixture.surah.number, surahVerseFixture.verse.verseNumber)

    @Before
    fun setUp() {
        coEvery { surahRepository.getSurah(any()) } returns surahFixture
        coEvery { surahVerseRepository.getSurahVersesFromSurah(any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseRepository.getSurahVersesFromJuz(any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseRepository.getSurahVersesFromHizb(any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseRepository.getSurahVersesFromPage(any()) } returns listOf(surahVerseFixture)
        coEvery { surahVerseRepository.getSurahVerse(any(), any()) } returns surahVerseFixture
        coEvery { surahVerseTranslationRepository.getSurahVerseTranslations(any(), any()) } returns surahVerseTranslationFixtures
        every { surahVersePreferencesRepository.getSurahVersePreferencesFlow() } returns flowOf(surahVersePreferencesFixture)
        coEvery { surahVersePreferencesRepository.getSurahVersePreferences() } returns surahVersePreferencesFixture
        coEvery { surahVersePreferencesRepository.setSurahVersePreferences(any()) } returns Unit
        every { surahVerseAudioRepository.downloadSurahVerseAudio(any(), any()) } returns flowOf(1)
        coEvery { surahVerseAudioRepository.getSurahVerseAudios(any(), any()) } returns listOf(surahVerseAudioFixture)
        coEvery { surahVerseAudioRepository.deleteSurahVerseAudios(any(), any()) } returns Unit

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
        val surahVersePlayerData = SurahVersePlayerData(
            reciter = surahVersePreferences.reciter!!,
            surahVerseAudios = listOf(surahVerseAudioFixture),
            state = SurahVersePlayerData.State.Idle
        )

        // Then
        assert(viewModel.uiState.value.surah == surah)
        assert(viewModel.uiState.value.surahVerses == surahVerses)
        assert(viewModel.uiState.value.surahVerseTranslations == surahVerseTranslations)
        assert(viewModel.uiState.value.juz == juz)
        assert(viewModel.uiState.value.hizb == hizb)
        assert(viewModel.uiState.value.page == page)
        assert(viewModel.uiState.value.surahVersePreferences == surahVersePreferences)
        assert(viewModel.uiState.value.informativeDisplayMode == informativeDisplayMode)
        assert(viewModel.uiState.value.surahVersePlayerData == surahVersePlayerData)
        assert(viewModel.uiState.value.audioDownloadProgress == null)
        assert(!viewModel.uiState.value.initializing)
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
    fun downloadAudio_should_download_surah_verse_audio() {
        // When
        viewModel.downloadAudio()

        // Then
        coVerify {
            surahVerseAudioRepository.downloadSurahVerseAudio(surahFixture, surahVersePreferencesFixture.reciter!!.id)
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
            surahVerseAudioRepository.deleteSurahVerseAudios(surahFixture.number,
                surahVersePreferencesFixture.reciter!!.id
            )
        }
    }

    @Test
    fun onPlaySurahVerseAudio_should_emit_download_request_when_surah_verse_audios_are_not_downloaded() = runTest {
        // Given
        coEvery { surahVerseAudioRepository.getSurahVerseAudios(any(), any()) } returns emptyList()
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
        viewModel.onPlaySurahVerseAudio(1)

        // Then
        assert(results.first() is SurahVerseViewModel.SurahVerseUiEvent.DownloadAudioRequest)
    }

    @Test
    fun onPlayAudio_should_play_SurahVerse_audio_when_surah_verse_audios_are_downloaded() {
        // Given
        val surahVerseAudios = List(surahFixture.totalVerses) { surahVerseAudioFixture }
        coEvery { surahVerseAudioRepository.getSurahVerseAudios(any(), any()) } returns surahVerseAudios
        val expectedResult = SurahVersePlayerData.State.Playing(surahVerseAudios.first())
        viewModel = SurahVerseViewModel(
            quranMode = quranModeFixture,
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseAudioRepository = surahVerseAudioRepository
        )

        // When
        viewModel.onPlaySurahVerseAudio(1)
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