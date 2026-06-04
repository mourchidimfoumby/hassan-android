package com.mfoumby.hassan.quran.domain

import com.mfoumby.hassan.common.domain.Language
import kotlinx.coroutines.flow.Flow

interface SurahRepository {
    fun getLocalSurahsFlow(): Flow<List<Surah>>

    suspend fun getLocalSurahs(): List<Surah>

    suspend fun getRemoteSurahs(language: Language): List<Surah>

    suspend fun upsertSurahs(surahs: List<Surah>)
}