package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.Reciter

interface ReciterRepository {
    suspend fun getReciters(): List<Reciter>

    suspend fun fetchReciterCount(): Int

    suspend fun downloadReciters()
}