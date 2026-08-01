package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.mapper.toLocal
import com.mfoumby.hassan.quran.data.mapper.toSurahVerse
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.Verse

class VerseLocalDataSource(private val verseDao: VerseDao) {
    suspend fun getSurahVerseFromNumber(surahNumber: Int): List<SurahVerse> =
        verseDao.getSurahVerseFromNumber(surahNumber).map { it.toSurahVerse() }

    suspend fun getSurahVerseFromPage(page: Int): List<SurahVerse> =
        verseDao.getSurahVerseFromPage(page).map { it.toSurahVerse() }

    suspend fun getVerseCount(): Int = verseDao.getVersesCount()

    suspend fun upsertVerses(verses: List<Verse>) =
        verseDao.upsertVerses(verses.map { it.toLocal() })
}