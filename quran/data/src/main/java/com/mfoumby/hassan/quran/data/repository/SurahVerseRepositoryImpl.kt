package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.quran.data.local.SurahVerseLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository

class SurahVerseRepositoryImpl(
    private val surahVerseRemoteDataSource: SurahVerseRemoteDataSource,
    private val surahVerseLocalDataSource: SurahVerseLocalDataSource
): SurahVerseRepository {
    companion object {
        const val MAX_SURAH_VERSES = 6236
    }

    override suspend fun getSurahVerses(surahNumber: Int): List<SurahVerse> = surahVerseLocalDataSource.getSurahVerses(surahNumber)

    override suspend fun getSurahVersesCount(): Int = surahVerseLocalDataSource.getSurahVersesCount()

    override suspend fun downloadSurahVerses() {
        surahVerseRemoteDataSource.getAllSurahVerses().collect {
            surahVerseLocalDataSource.upsertSurahVerses(it)
        }
    }
}