package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.mapper.toSurahVerseTranslation
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SurahVerseTranslationRemoteDataSource(private val surahVerseTranslationApi: SurahVerseTranslationApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun getSurahVerseTranslations(language: Language): Flow<List<SurahVerseTranslation>> =
        surahVerseTranslationApi.getAllSurahVerseTranslations(language.getLanguageCode())
            .map { surahVerseTranslations ->
                surahVerseTranslations.map { it.toSurahVerseTranslation(language) }
            }

    suspend fun getSurahVerseTranslations(surahNumber: Int, language: Language): List<SurahVerseTranslation> = withContext(dispatcher) {
        surahVerseTranslationApi.getSurahVerseTranslations(surahNumber, language.getLanguageCode()).map { it.toSurahVerseTranslation(language) }
    }
}