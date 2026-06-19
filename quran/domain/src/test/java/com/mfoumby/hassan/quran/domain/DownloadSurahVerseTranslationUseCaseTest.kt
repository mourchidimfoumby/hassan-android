package com.mfoumby.hassan.quran.domain

import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.common.domain.translationLanguageFixture
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import com.mfoumby.hassan.quran.domain.usecase.DownloadSurahVerseTranslationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DownloadSurahVerseTranslationUseCaseTest {
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository = mockk()
    private val surahVerseTranslationLanguageRepository: SurahVerseTranslationLanguageRepository = mockk()

    private lateinit var useCase: DownloadSurahVerseTranslationUseCase

    @Before
    fun setUp() {
        coEvery { surahVerseTranslationRepository.downloadSurahVerseTranslations(any()) } returns flowOf(listOf(surahVerseTranslationFixture))
        coEvery { surahVerseTranslationLanguageRepository.updateTranslationLanguage(any()) } returns Unit

        useCase = DownloadSurahVerseTranslationUseCase(
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahVerseTranslationLanguageRepository = surahVerseTranslationLanguageRepository
        )
    }

    @Test
    fun execute_should_not_do_anything_when_translation_language_state_is_different_than_not_downloaded() = runTest {
        // When
        useCase.execute(translationLanguageFixture)

        // Then
        coVerify(exactly = 0) { surahVerseTranslationRepository.downloadSurahVerseTranslations(any()).collect() }
        coVerify(exactly = 0) { surahVerseTranslationLanguageRepository.updateTranslationLanguage(any()) }
    }

    @Test
    fun execute_should_update_translation_language_state_to_downloaded_when_success() = runTest {
        // When
        useCase.execute(translationLanguageFixture.copy(state = TranslationLanguageState.NotDownloaded))

        // Then
        coVerify {
            surahVerseTranslationLanguageRepository.updateTranslationLanguage(
                translationLanguageFixture.copy(state = TranslationLanguageState.Downloaded)
            )
        }
    }
}