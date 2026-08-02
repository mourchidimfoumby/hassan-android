package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.mapper.toJuz
import com.mfoumby.hassan.quran.data.mapper.toLocal
import com.mfoumby.hassan.quran.data.mapper.toSurahVerse
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.Verse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SurahVerseLocalDataSource(private val verseDao: VerseDao) {
    fun getAllJuz(): Flow<List<Juz>> = verseDao.getAllJuz().map {
        it.map { localJuz -> localJuz.toJuz() }
    }

    suspend fun getSurahVerseFromNumber(surahNumber: Int, limit: Int): List<SurahVerse> =
        verseDao.getSurahVerseFromNumber(surahNumber, limit).map { it.toSurahVerse() }

    suspend fun getSurahVerseFromJuzNumber(juzNumber: Int, limit: Int): List<SurahVerse> =
        verseDao.getSurahVerseFromJuzNumber(juzNumber, limit).map { it.toSurahVerse() }

    suspend fun getSurahVerseFromPage(page: Int): List<SurahVerse> =
        verseDao.getSurahVerseFromPage(page).map { it.toSurahVerse() }

    suspend fun getVerseCount(): Int = verseDao.getVersesCount()

    suspend fun upsertVerses(verses: List<Verse>) =
        verseDao.upsertVerses(verses.map { it.toLocal() })
}