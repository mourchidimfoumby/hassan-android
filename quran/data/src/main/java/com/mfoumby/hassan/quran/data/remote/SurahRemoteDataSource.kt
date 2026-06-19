package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.mapper.toSurah
import com.mfoumby.hassan.quran.domain.entity.Surah
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SurahRemoteDataSource(private val surahApi: SurahApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getSurahs(language: Language): List<Surah> = withContext(dispatcher) {
        val surahs = surahApi.getSurahs()
        val surahTranslations = surahApi.getSurahTranslations(language.code).sortedBy { it.number }
        return@withContext surahs.map {
            it.toSurah(surahTranslations[it.number - 1].translation)
        }
    }
}