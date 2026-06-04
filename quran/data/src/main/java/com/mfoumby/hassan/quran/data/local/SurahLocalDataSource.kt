package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.toLocal
import com.mfoumby.hassan.quran.data.toSurah
import com.mfoumby.hassan.quran.domain.Surah
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SurahLocalDataSource(private val surahDao: SurahDao) {
    fun getSurahsFlow(): Flow<List<Surah>> = surahDao.getSurahsFlow()
        .map { surahs -> surahs.map { it.toSurah() } }

    suspend fun getSurahs(): List<Surah> = surahDao.getSurahs().map { it.toSurah() }

    suspend fun upsertSurahs(surahs: List<Surah>) =
        surahDao.upsertSurahs(surahs.map { it.toLocal() })
}