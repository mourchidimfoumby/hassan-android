package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.Hizb
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import kotlinx.coroutines.flow.Flow

interface SurahVerseRepository {
    fun getAllJuz(): Flow<List<Juz>>

    fun getAllHizb(): Flow<List<Hizb>>

    suspend fun getSurahVersesFromSurah(surahNumber: Int, limit: Int = Int.MAX_VALUE): List<SurahVerse>

    suspend fun getSurahVersesFromJuz(juzNumber: Int, limit: Int = Int.MAX_VALUE): List<SurahVerse>

    suspend fun getSurahVersesFromHizb(hizbNumber: Int, limit: Int = Int.MAX_VALUE): List<SurahVerse>

    suspend fun getSurahVersesFromPage(page: Int): List<SurahVerse>

    suspend fun getVerseCount(): Int

    suspend fun downloadVerses()
}