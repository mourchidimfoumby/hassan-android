package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.quran.data.model.RemoteVerse
import kotlinx.coroutines.flow.Flow

interface VerseApi {
    fun getAllVerses(): Flow<List<RemoteVerse>>

    suspend fun getVerses(surahNumber: Int): List<RemoteVerse>
}