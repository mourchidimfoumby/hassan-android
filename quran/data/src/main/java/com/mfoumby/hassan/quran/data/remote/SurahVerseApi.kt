package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.quran.data.model.RemoteSurahVerse
import kotlinx.coroutines.flow.Flow

interface SurahVerseApi {
    fun getAllSurahVerses(): Flow<List<RemoteSurahVerse>>

    suspend fun getSurahVerses(surahNumber: Int): List<RemoteSurahVerse>
}