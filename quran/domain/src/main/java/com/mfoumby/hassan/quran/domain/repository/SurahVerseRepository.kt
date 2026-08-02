package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import kotlinx.coroutines.flow.Flow

interface SurahVerseRepository {
    fun getAllJuz(): Flow<List<Juz>>

    suspend fun getSurahVersesFromSurahNumber(surahNumber: Int, limit: Int = Int.MAX_VALUE): List<SurahVerse>

    suspend fun getSurahVersesFromJuzNumber(juzNumber: Int, limit: Int = Int.MAX_VALUE): List<SurahVerse>

    suspend fun getSurahVersesFromPage(page: Int): List<SurahVerse>

    suspend fun getVerseCount(): Int

    suspend fun downloadVerses()
}