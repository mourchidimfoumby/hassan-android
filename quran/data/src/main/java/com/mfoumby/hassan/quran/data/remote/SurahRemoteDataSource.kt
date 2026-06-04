package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.common.domain.Language
import com.mfoumby.hassan.quran.data.toSurah
import com.mfoumby.hassan.quran.domain.Surah
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SurahRemoteDataSource(private val surahApi: SurahApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getSurahs(language: Language): List<Surah> = withContext(dispatcher) {
        val surahs = surahApi.getSurahs()
        val surahTranslations = surahApi.getSurahTranslations(mapLanguage(language)).sortedBy { it.number }
        return@withContext surahs.map {
            it.toSurah(surahTranslations[it.number - 1].translation)
        }
    }

    private fun mapLanguage(language: Language): String = when(language) {
        Language.ENGLISH -> "en"
        Language.FRENCH -> "fr"
    }
}