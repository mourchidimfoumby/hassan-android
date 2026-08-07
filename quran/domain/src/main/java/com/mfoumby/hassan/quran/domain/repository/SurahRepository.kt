package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.quran.domain.entity.Surah
import kotlinx.coroutines.flow.Flow

interface SurahRepository {
    fun getSurahs(): Flow<List<Surah>>

    suspend fun getSurah(surahNumber: Int): Surah?

    suspend fun getSurahCount(): Int

    suspend fun downloadSurahs(language: Language)
}