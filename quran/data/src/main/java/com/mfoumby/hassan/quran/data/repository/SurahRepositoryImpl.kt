package com.mfoumby.hassan.quran.data.repository

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.data.local.SurahLocalDataSource
import com.mfoumby.hassan.quran.data.remote.SurahRemoteDataSource
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import kotlinx.coroutines.flow.Flow

class SurahRepositoryImpl(
    private val surahRemoteDataSource: SurahRemoteDataSource,
    private val surahLocalDataSource: SurahLocalDataSource
): SurahRepository {
    companion object {
        const val MAX_SURAH_COUNT = 114
    }

    override fun getSurahs(): Flow<List<Surah>> = surahLocalDataSource.getSurahsFlow()

    override suspend fun getSurah(surahNumber: Int): Surah? = surahLocalDataSource.getSurah(surahNumber)

    override suspend fun getSurahCount(): Int = surahLocalDataSource.getSurahCount()

    override suspend fun searchSurah(name: String): List<Surah> = surahLocalDataSource.searchSurah(name)

    override suspend fun downloadSurahs(language: Language) {
        val surahs = surahRemoteDataSource.getSurahs(language)
        surahLocalDataSource.upsertSurahs(surahs)
    }
}