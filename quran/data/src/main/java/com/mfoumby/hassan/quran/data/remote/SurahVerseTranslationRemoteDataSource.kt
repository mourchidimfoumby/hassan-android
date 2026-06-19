package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.mapper.toSurahVerseTranslation
import com.mfoumby.hassan.quran.domain.entity.SurahVerseTranslation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SurahVerseTranslationRemoteDataSource(
    private val surahVerseTranslationApi: SurahVerseTranslationApi
) {
    fun getSurahVerseTranslations(language: Language): Flow<List<SurahVerseTranslation>> =
        surahVerseTranslationApi.getAllSurahVerseTranslations(language.code)
            .map { surahVerseTranslations ->
                surahVerseTranslations.map { it.toSurahVerseTranslation(language) }
            }
}