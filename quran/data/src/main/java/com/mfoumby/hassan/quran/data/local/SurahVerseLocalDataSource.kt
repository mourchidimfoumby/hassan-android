package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.mapper.toLocal
import com.mfoumby.hassan.quran.data.mapper.toSurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerse

class SurahVerseLocalDataSource(private val surahVerseDao: SurahVerseDao) {
    suspend fun getSurahVerses(surahNumber: Int): List<SurahVerse> =
        surahVerseDao.getSurahVerses(surahNumber).map { it.toSurahVerse() }

    suspend fun getSurahVersesCount(): Int = surahVerseDao.getSurahVersesCount()

    suspend fun upsertSurahVerses(surahVerses: List<SurahVerse>) =
        surahVerseDao.upsertSurahVerses(surahVerses.map { it.toLocal() })
}