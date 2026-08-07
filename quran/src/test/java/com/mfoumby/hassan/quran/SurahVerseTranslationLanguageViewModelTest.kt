package com.mfoumby.hassan.quran

import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.translationLanguageFixture
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import com.mfoumby.hassan.quran.domain.surahVersePreferencesFixture
import com.mfoumby.hassan.quran.domain.usecase.DeleteTranslationLanguageUseCase
import com.mfoumby.hassan.quran.domain.usecase.DownloadSurahVerseTranslationUseCase
import com.mfoumby.hassan.quran.ui.surahverse.surahversetranslationlanguage.SurahVerseTranslationLanguageViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SurahVerseTranslationLanguageViewModelTest {
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository = mockk()
    private val surahVerseTranslationLanguageRepository: SurahVerseTranslationLanguageRepository = mockk()
    private val downloadSurahVerseTranslationUseCase: DownloadSurahVerseTranslationUseCase = mockk()
    private val deleteTranslationLanguageUseCase: DeleteTranslationLanguageUseCase = mockk()

    lateinit var viewModel: SurahVerseTranslationLanguageViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        coEvery { surahVersePreferencesRepository.getSurahVersePreferencesFlow() } returns flowOf(surahVersePreferencesFixture)
        coEvery { surahVersePreferencesRepository.setSurahVersePreferences(any()) } returns Unit
        coEvery { surahVerseTranslationLanguageRepository.getTranslationLanguagesFlow() } returns flowOf(listOf(translationLanguageFixture))
        coEvery { downloadSurahVerseTranslationUseCase.execute(any()) } returns emptyFlow()
        coEvery { deleteTranslationLanguageUseCase.execute(any()) } returns Unit

        Dispatchers.setMain(testDispatcher)

        viewModel = SurahVerseTranslationLanguageViewModel(
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseTranslationLanguageRepository = surahVerseTranslationLanguageRepository,
            downloadSurahVerseTranslationUseCase = downloadSurahVerseTranslationUseCase,
            deleteTranslationLanguageUseCase = deleteTranslationLanguageUseCase
        )
    }

    @Test
    fun init_should_init_values() {
        // Given
        val preferences = surahVersePreferencesFixture
        val translationLanguages = listOf(translationLanguageFixture)
        val isLoading = false

        // Then
        assert(viewModel.uiState.value.preferences == preferences)
        assert(viewModel.uiState.value.translationLanguages == translationLanguages)
        assert(viewModel.uiState.value.isLoading == isLoading)
    }

    @Test
    fun onTranslationLanguageSelect_should_set_translation_language_preference_when_is_not_set() = runTest {
        // Given
        val translationLanguage = translationLanguageFixture.copy(state = TranslationLanguage.TranslationLanguageState.Downloaded)
        val surahVersePreferences = surahVersePreferencesFixture.copy(translationLanguage = null)

        coEvery { surahVersePreferencesRepository.getSurahVersePreferencesFlow() } returns flowOf(surahVersePreferences)

        viewModel = SurahVerseTranslationLanguageViewModel(
            surahVersePreferencesRepository = surahVersePreferencesRepository,
            surahVerseTranslationLanguageRepository = surahVerseTranslationLanguageRepository,
            downloadSurahVerseTranslationUseCase = downloadSurahVerseTranslationUseCase,
            deleteTranslationLanguageUseCase = deleteTranslationLanguageUseCase
        )

        // When
        viewModel.onTranslationLanguageSelect(translationLanguage)

        // Then
        coVerify { surahVersePreferencesRepository.setSurahVersePreferences(surahVersePreferences.copy(translationLanguage = translationLanguage.language)) }
    }

    @Test
    fun onTranslationLanguageSelect_should_remove_translation_language_preference_when_already_set() {
        // Given
        val translationLanguage = translationLanguageFixture.copy(state = TranslationLanguage.TranslationLanguageState.Downloaded)
        val surahVersePreferences = surahVersePreferencesFixture.copy(translationLanguage = translationLanguage.language)
        coEvery { surahVersePreferencesRepository.getSurahVersePreferences() } returns surahVersePreferences

        // When
        viewModel.onTranslationLanguageSelect(translationLanguage)

        // Then
        coVerify { surahVersePreferencesRepository.setSurahVersePreferences(surahVersePreferences.copy(translationLanguage = null)) }
    }

    @Test
    fun onTranslationLanguageSelect_should_download_translation_language_preference_when_not_downloaded() = runTest {
        // Given
        val translationLanguage = translationLanguageFixture.copy(state = TranslationLanguage.TranslationLanguageState.NotDownloaded)
        val surahVersePreferences = surahVersePreferencesFixture.copy(translationLanguage = null)
        coEvery { surahVersePreferencesRepository.getSurahVersePreferences() } returns surahVersePreferences

        // When
        viewModel.onTranslationLanguageSelect(translationLanguage)
        advanceUntilIdle()

        // Then
        coVerify { downloadSurahVerseTranslationUseCase.execute(any()) }
    }

    @Test
    fun onDeleteTranslationLanguage_should_delete_translation_language() {
        // Given
        val translationLanguage = translationLanguageFixture

        // When
        viewModel.onDeleteTranslationLanguage(translationLanguage)

        // Then
        coVerify { deleteTranslationLanguageUseCase.execute(translationLanguage) }
    }
}