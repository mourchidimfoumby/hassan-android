package com.mfoumby.hassan.quran

import com.mfoumby.hassan.quran.domain.hizbFixtures
import com.mfoumby.hassan.quran.domain.juzFixtures
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.surahFixtures
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.ui.QuranViewModel
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
class QuranViewModelTest {
    private val surahRepository: SurahRepository = mockk()
    private val surahVerseRepository: SurahVerseRepository = mockk()
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository = mockk()

    lateinit var viewModel: QuranViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        every { surahRepository.getSurahs() } returns flowOf(surahFixtures)
        every { surahVerseRepository.getAllJuz() } returns flowOf(juzFixtures)
        every { surahVerseRepository.getAllHizb() } returns flowOf(hizbFixtures)
        every { surahVersePreferencesRepository.getSurahVersePreferencesFlow() } returns flowOf(surahVersePreferencesFixture)

        Dispatchers.setMain(testDispatcher)

        viewModel = QuranViewModel(
            surahRepository = surahRepository,
            surahVerseRepository = surahVerseRepository,
            surahVersePreferencesRepository = surahVersePreferencesRepository
        )
    }

    @Test
    fun init_should_init_values() {
        // Given
        val surahs = surahFixtures
        val allJuz = juzFixtures
        val allHizb = hizbFixtures
        val preferences = surahVersePreferencesFixture
        val isLoading = false

        // Then
        assert(viewModel.uiState.value.surahs == surahs)
        assert(viewModel.uiState.value.allJuz == allJuz)
        assert(viewModel.uiState.value.allHizb == allHizb)
        assert(viewModel.uiState.value.preferences == preferences)
        assert(viewModel.uiState.value.isLoading == isLoading)
    }
}