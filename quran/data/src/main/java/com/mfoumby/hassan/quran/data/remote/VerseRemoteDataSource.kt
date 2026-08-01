package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.quran.data.mapper.toVerse
import com.mfoumby.hassan.quran.domain.entity.Verse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class VerseRemoteDataSource(private val verseApi: VerseApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun getAllVerses(): Flow<List<Verse>> =
        verseApi.getAllVerses()
            .map { surahVerses ->
                surahVerses.map { it.toVerse() }
            }

    suspend fun getVerses(surahNumber: Int): List<Verse> = withContext(dispatcher) {
        verseApi.getVerses(surahNumber).map { it.toVerse() }
    }
}