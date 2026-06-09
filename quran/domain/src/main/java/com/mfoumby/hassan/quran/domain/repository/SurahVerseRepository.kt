package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.SurahVerse

interface SurahVerseRepository {
    suspend fun getSurahVerses(surahNumber: Int): List<SurahVerse>

    suspend fun getSurahVersesCount(): Int

    suspend fun downloadSurahVerses()
}