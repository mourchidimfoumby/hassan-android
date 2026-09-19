package com.mfoumby.hassan.quran.domain

import com.mfoumby.hassan.common.domain.translationLanguageFixture
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import com.mfoumby.hassan.quran.domain.usecase.DeleteTranslationLanguageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteTranslationLanguageUseCaseTest {
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository = mockk()
    private val surahVerseTranslationLanguageRepository: SurahVerseTranslationLanguageRepository = mockk()
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository = mockk()

    private lateinit var useCase: DeleteTranslationLanguageUseCase

        @Before
        fun setUp() {
            coEvery { surahVerseTranslationRepository.deleteSurahVerseTranslation(any()) } returns Unit
            coEvery { surahVerseTranslationLanguageRepository.updateTranslationLanguage(any()) } returns Unit
            coEvery { surahVersePreferencesRepository.getSurahVersePreferences() } returns surahVersePreferencesFixture
            coEvery { surahVersePreferencesRepository.setSurahVersePreferences(any()) } returns Unit

            useCase = DeleteTranslationLanguageUseCase(
                surahVerseTranslationRepository = surahVerseTranslationRepository,
                surahVerseTranslationLanguageRepository = surahVerseTranslationLanguageRepository,
                surahVersePreferencesRepository = surahVersePreferencesRepository
            )
        }

        @Test
        fun execute_should_delete_surah_verse_translation() = runTest {
            // When
            useCase.execute(translationLanguageFixture)

            // Then
            coVerify { surahVerseTranslationRepository.deleteSurahVerseTranslation(any()) }
        }

        @Test
        fun execute_should_update_translation_language_with_not_downloaded_state() = runTest {
            // When
            useCase.execute(translationLanguageFixture)

            // Then
            coVerify { surahVerseTranslationLanguageRepository.updateTranslationLanguage(any()) }
        }

        @Test
        fun execute_should_update_surah_verse_preferences_with_null_translation_language_if_current() = runTest {
            // Given
            val surahVersePreferences = surahVersePreferencesFixture.copy(translationLanguage = translationLanguageFixture.language)
            coEvery { surahVersePreferencesRepository.getSurahVersePreferences() } returns surahVersePreferences

            // When
            useCase.execute(translationLanguageFixture)

            // Then
            coVerify { surahVersePreferencesRepository.setSurahVersePreferences(surahVersePreferences.copy(translationLanguage = null)) }
        }
}