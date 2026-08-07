package com.mfoumby.hassan.quran

import com.mfoumby.hassan.quran.domain.reciterFixtures
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.ui.reciters.RecitersViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecitersViewModelTest {
    private val reciterRepository: ReciterRepository = mockk()
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository = mockk()

    lateinit var viewModel: RecitersViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        coEvery { reciterRepository.getReciters() } returns reciterFixtures
        coEvery { surahVersePreferencesRepository.setSurahVersePreferences(any()) } returns Unit
        every { surahVersePreferencesRepository.getSurahVersePreferencesFlow() } returns flowOf(surahVersePreferencesFixture)

        Dispatchers.setMain(testDispatcher)

        viewModel = RecitersViewModel(
            reciterRepository = reciterRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository
        )
    }

    @Test
    fun init_should_init_values() {
        // Given
        val reciters = reciterFixtures
        val surahVersePreferences = surahVersePreferencesFixture

        // Then
        assert(viewModel.uiState.value.reciters == reciters)
        assert(viewModel.uiState.value.preferences == surahVersePreferences)
        assert(!viewModel.uiState.value.isLoading)
    }

    @Test
    fun onReciterClick_should_update_surah_verse_preferences() {
        // Given
        val reciter = reciterFixtures[1]

        // When
        viewModel.onReciterClick(reciter)

        // Then
        coVerify {
            surahVersePreferencesRepository.setSurahVersePreferences(
                surahVersePreferencesFixture.copy(reciter = reciter)
            )
        }
    }
}