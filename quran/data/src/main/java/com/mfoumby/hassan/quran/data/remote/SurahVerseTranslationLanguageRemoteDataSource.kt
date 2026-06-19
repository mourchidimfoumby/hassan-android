package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.common.data.mapper.toLanguage
import com.mfoumby.hassan.common.domain.entity.Language
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SurahVerseTranslationLanguageRemoteDataSource(
    private val surahVerseTranslationLanguageApi: SurahVerseTranslationLanguageApi
) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getTranslationLanguages(): List<Language> = withContext(dispatcher) {
        surahVerseTranslationLanguageApi.getTranslationLanguages().map { it.toLanguage() }
    }
}