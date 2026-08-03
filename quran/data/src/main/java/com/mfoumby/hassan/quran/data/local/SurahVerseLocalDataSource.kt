package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.mapper.toHizb
import com.mfoumby.hassan.quran.data.mapper.toJuz
import com.mfoumby.hassan.quran.data.mapper.toLocal
import com.mfoumby.hassan.quran.data.mapper.toSurahVerse
import com.mfoumby.hassan.quran.domain.entity.Hizb
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.entity.Verse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SurahVerseLocalDataSource(private val verseDao: VerseDao) {
    fun getAllJuz(): Flow<List<Juz>> = verseDao.getAllJuz().map {
        it.map { localJuz -> localJuz.toJuz() }
    }

    fun getAllHizb(): Flow<List<Hizb>> = verseDao.getAllHizb().map {
        it.map { localHizb -> localHizb.toHizb() }
    }

    suspend fun getSurahVerseFromSurah(surahNumber: Int, limit: Int): List<SurahVerse> =
        verseDao.getSurahVerseFromSurah(surahNumber, limit).map { it.toSurahVerse() }

    suspend fun getSurahVersesFromJuz(juzNumber: Int, limit: Int): List<SurahVerse> =
        verseDao.getSurahVersesFromJuz(juzNumber, limit).map { it.toSurahVerse() }

    suspend fun getSurahVersesFromHizb(hizbNumber: Int, limit: Int): List<SurahVerse> =
        verseDao.getSurahVersesFromHizb(hizbNumber, limit).map { it.toSurahVerse() }

    suspend fun getSurahVersesFromPage(page: Int): List<SurahVerse> =
        verseDao.getSurahVersesFromPage(page).map { it.toSurahVerse() }

    suspend fun getSurahVerse(surahNumber: Int, verseNumber: Int): SurahVerse? =
        verseDao.getSurahVerse(surahNumber, verseNumber)?.toSurahVerse()

    suspend fun getVerseCount(): Int = verseDao.getVersesCount()

    suspend fun upsertVerses(verses: List<Verse>) =
        verseDao.upsertVerses(verses.map { it.toLocal() })
}