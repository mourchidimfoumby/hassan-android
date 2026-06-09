package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.quran.data.mapper.toSurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SurahVerseRemoteDataSource(private val surahVerseApi: SurahVerseApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun getAllSurahVerses(): Flow<List<SurahVerse>> =
        surahVerseApi.getAllSurahVerses()
            .map { surahVerses ->
                surahVerses.map { it.toSurahVerse() }
            }

    suspend fun getSurahVerses(surahNumber: Int): List<SurahVerse> = withContext(dispatcher) {
        surahVerseApi.getSurahVerses(surahNumber).map { it.toSurahVerse() }
    }
}