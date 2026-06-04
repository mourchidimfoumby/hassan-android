package com.mfoumby.hassan.quran.data.remote

import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.common.domain.Language
import com.mfoumby.hassan.quran.data.local.SurahLocalDataSource
import com.mfoumby.hassan.quran.domain.Surah
import com.mfoumby.hassan.quran.domain.SurahRepository
import kotlinx.coroutines.flow.Flow

class SurahRepositoryImpl(
    private val surahRemoteDataSource: SurahRemoteDataSource,
    private val surahLocalDataSource: SurahLocalDataSource
): SurahRepository {
    override fun getLocalSurahsFlow(): Flow<List<Surah>> = surahLocalDataSource.getSurahsFlow()

    override suspend fun getLocalSurahs(): List<Surah> = surahLocalDataSource.getSurahs()

    override suspend fun getRemoteSurahs(language: Language): List<Surah> =
        runCatching { surahRemoteDataSource.getSurahs(language) }
            .onFailure { e( "Error getting remote surahs", it) }
            .getOrThrow()

    override suspend fun upsertSurahs(surahs: List<Surah>) {
        surahLocalDataSource.upsertSurahs(surahs)
    }
}