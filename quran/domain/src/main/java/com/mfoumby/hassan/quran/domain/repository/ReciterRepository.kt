package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.Reciter
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import kotlinx.coroutines.flow.Flow

interface ReciterRepository {
    suspend fun getReciters(): List<Reciter>

    suspend fun fetchReciterCount(): Int

    suspend fun getSurahVerseAudios(surahNumber: Int, reciterId: String): List<SurahVerseAudio>

    suspend fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String)

    suspend fun downloadReciters()

    fun downloadSurahVerseAudio(surah: Surah, reciterId: String): Flow<Int>
}