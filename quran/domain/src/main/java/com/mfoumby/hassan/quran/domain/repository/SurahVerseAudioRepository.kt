package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import kotlinx.coroutines.flow.Flow

interface SurahVerseAudioRepository {
    suspend fun getSurahVerseAudios(surah: Surah, reciterId: String, offset: Int = 0, limit: Int = Int.MAX_VALUE): List<SurahVerseAudio>

    suspend fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String)

    fun downloadSurahVerseAudio(surah: Surah, reciterId: String): Flow<Int>

    suspend fun isSurahVerseAudioDownloaded(surah: Surah, reciterId: String): Boolean
}