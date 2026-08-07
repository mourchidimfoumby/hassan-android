package com.mfoumby.hassan.quran.domain

import com.mfoumby.hassan.common.domain.entity.Progress
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.common.domain.translationLanguageFixture
import com.mfoumby.hassan.quran.domain.entity.Constants.TOTAL_QURAN_VERSES
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import com.mfoumby.hassan.quran.domain.usecase.DownloadSurahVerseTranslationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

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
        useCase.execute(translationLanguageFixture).collect()

        // Then
        coVerify(exactly = 0) { surahVerseTranslationRepository.downloadSurahVerseTranslations(any()).collect() }
        coVerify(exactly = 0) { surahVerseTranslationLanguageRepository.updateTranslationLanguage(any()) }
    }

    @Test
    fun execute_should_emit_downloading_progress() = runTest {
        // Given
        val number = 10
        val translationLanguage = translationLanguageFixture.copy(state = TranslationLanguageState.NotDownloaded)
        val surahVerseTranslations = List(number) { surahVerseTranslationFixture }
        val expectedResult = translationLanguage.copy(
            state = TranslationLanguageState.Downloading(Progress(number, TOTAL_QURAN_VERSES).progress)
        )

        coEvery { surahVerseTranslationRepository.downloadSurahVerseTranslations(any()) } returns flowOf(surahVerseTranslations)

        // When
        val result = useCase.execute(translationLanguage)

        // Then
        assertEquals(expectedResult, result.first())
    }

    @Test
    fun execute_should_update_translation_language_state_to_downloaded_when_success() = runTest {
        // Given
        val translationLanguage = translationLanguageFixture.copy(state = TranslationLanguageState.NotDownloaded)

        // When
        useCase.execute(translationLanguage).collect()

        // Then
        coVerify {
            surahVerseTranslationLanguageRepository.updateTranslationLanguage(
                translationLanguage.copy(state = TranslationLanguageState.Downloaded)
            )
        }
    }

    @Test
    fun execute_should_update_translation_language_state_to_not_downloaded_when_fails() = runTest {
        // Given
        val translationLanguage = translationLanguageFixture.copy(state = TranslationLanguageState.NotDownloaded)
        every { surahVerseTranslationRepository.downloadSurahVerseTranslations(any()) } returns flow { error("") }

        // When
        runCatching {
            useCase.execute(translationLanguage).collect()
        }

        // Then
        coVerify {
            surahVerseTranslationLanguageRepository.updateTranslationLanguage(
                translationLanguage.copy(state = TranslationLanguageState.NotDownloaded)
            )
        }
    }
}