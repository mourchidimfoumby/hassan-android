package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.quran.data.local.SurahVerseLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahVerseRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.Hizb
import com.mfoumby.hassan.quran.domain.entity.Juz
import com.mfoumby.hassan.quran.domain.entity.SurahVerse
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import kotlinx.coroutines.flow.Flow

class SurahVerseRepositoryImpl(
    private val surahVerseRemoteDataSource: SurahVerseRemoteDataSource,
    private val surahVerseLocalDataSource: SurahVerseLocalDataSource
): SurahVerseRepository {
    override fun getAllJuz(): Flow<List<Juz>> = surahVerseLocalDataSource.getAllJuz()

    override fun getAllHizb(): Flow<List<Hizb>> = surahVerseLocalDataSource.getAllHizb()

    override suspend fun getSurahVersesFromSurah(surahNumber: Int, limit: Int): List<SurahVerse> =
        surahVerseLocalDataSource.getSurahVerseFromSurah(surahNumber, limit)

    override suspend fun getSurahVersesFromJuz(juzNumber: Int, limit: Int): List<SurahVerse> =
        surahVerseLocalDataSource.getSurahVerseFromJuz(juzNumber, limit)

    override suspend fun getSurahVersesFromHizb(hizbNumber: Int, limit: Int): List<SurahVerse> =
        surahVerseLocalDataSource.getSurahVerseFromHizb(hizbNumber, limit)

    override suspend fun getSurahVersesFromPage(page: Int): List<SurahVerse> =
        surahVerseLocalDataSource.getSurahVerseFromPage(page)

    override suspend fun getVerseCount(): Int = surahVerseLocalDataSource.getVerseCount()

    override suspend fun downloadVerses() {
        surahVerseRemoteDataSource.getAllVerses().collect {
            surahVerseLocalDataSource.upsertVerses(it)
        }
    }
}