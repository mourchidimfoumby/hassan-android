package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.SurahVerse

interface SurahVerseRepository {
    suspend fun getSurahVerseFromSurahNumber(surahNumber: Int): List<SurahVerse>

    suspend fun getSurahVerseFromPage(page: Int): List<SurahVerse>

    suspend fun getVerseCount(): Int

    suspend fun downloadVerses()
}