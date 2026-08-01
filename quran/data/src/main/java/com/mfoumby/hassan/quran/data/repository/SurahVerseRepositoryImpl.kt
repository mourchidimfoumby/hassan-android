package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.quran.data.local.VerseLocalDataSource
import com.mfoumby.hassan.quran.data.remote.VerseRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository

class SurahVerseRepositoryImpl(
    private val verseRemoteDataSource: VerseRemoteDataSource,
    private val verseLocalDataSource: VerseLocalDataSource
): SurahVerseRepository {
    override suspend fun getSurahVerseFromSurahNumber(surahNumber: Int): List<SurahVerse> =
        verseLocalDataSource.getSurahVerseFromNumber(surahNumber)

    override suspend fun getSurahVerseFromPage(page: Int): List<SurahVerse> =
        verseLocalDataSource.getSurahVerseFromPage(page)

    override suspend fun getVerseCount(): Int = verseLocalDataSource.getVerseCount()

    override suspend fun downloadVerses() {
        verseRemoteDataSource.getAllVerses().collect {
            verseLocalDataSource.upsertVerses(it)
        }
    }
}