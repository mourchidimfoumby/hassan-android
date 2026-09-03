package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.mapper.toLocal
import com.mfoumby.hassan.quran.data.mapper.toSurah
import com.mfoumby.hassan.quran.domain.entity.Surah
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SurahLocalDataSource(private val surahDao: SurahDao) {
    private val dispatcher = Dispatchers.IO
    fun getSurahsFlow(): Flow<List<Surah>> = surahDao.getSurahs()
        .map { surahs -> surahs.map { it.toSurah() } }

    suspend fun getSurah(surahNumber: Int): Surah? = withContext(dispatcher) {
        surahDao.getSurah(surahNumber)?.toSurah()
    }

    suspend fun getSurahCount(): Int = withContext(dispatcher) {
        surahDao.getSurahCount()
    }

    suspend fun searchSurah(name: String): List<Surah> = withContext(dispatcher) {
        surahDao.searchSurah(name).map { it.toSurah() }
    }

    suspend fun upsertSurahs(surahs: List<Surah>) {
        withContext(dispatcher) {
            surahDao.upsertSurahs(surahs.map { it.toLocal() })
        }
    }
}