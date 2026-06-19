package com.mfoumby.hassan.common.domain

import com.mfoumby.hassan.common.domain.usecase.GetCurrentLanguageUseCase
import org.junit.Before
import org.junit.Test
import java.util.Locale

class GetCurrentLanguageUseCaseTest {
    private lateinit var useCase: GetCurrentLanguageUseCase

    @Before
    fun setUp() {
        useCase = GetCurrentLanguageUseCase()
    }

    @Test
    fun execute_should_return_current_language() {
        // Given
        val expected = Locale.getDefault().language

        // When
        val result = useCase.execute()

        // Then
        assert(result.code == expected)
    }
}