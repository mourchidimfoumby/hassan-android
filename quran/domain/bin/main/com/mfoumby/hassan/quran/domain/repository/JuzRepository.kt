package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.Juz
import kotlinx.coroutines.flow.Flow

interface JuzRepository {
    fun getAllJuz(): Flow<List<Juz>>
}