package com.mfoumby.hassan.quran.domain

import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import com.mfoumby.hassan.quran.domain.usecase.GetSurahVerseFlowUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetSurahVerseFlowUseCaseTest {
    private val surahVerseRepository: SurahVerseRepository = mockk()
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository = mockk()
    private val surahPreferencesRepository: SurahVersePreferencesRepository = mockk()

    private lateinit var useCase: GetSurahVerseFlowUseCase

    @Before
    fun setUp() {
        coEvery { surahVerseRepository.getSurahVerses(any()) } returns surahVerseFixtures
        coEvery { surahVerseTranslationRepository.getSurahVerseTranslations(any(), any()) } returns surahVerseTranslationFixtures
        coEvery { surahPreferencesRepository.getSurahVersePreferencesFlow() } returns flowOf(surahVersePreferencesFixture)

        useCase = GetSurahVerseFlowUseCase(
            surahVerseRepository = surahVerseRepository,
            surahVerseTranslationRepository = surahVerseTranslationRepository,
            surahPreferencesRepository = surahPreferencesRepository
        )
    }

    @Test
    fun execute_should_return_surah_verses_with_translation() = runTest {
        // Given
        val expected = surahVerseFixtures.mapIndexed { index, surahVerse ->
            surahVerse.copy(translation = surahVerseTranslationFixtures[index].translation)
        }

        // When
        val result = useCase.execute(1).first()

        // Then
        assert(result == expected)
    }
}