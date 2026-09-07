package com.mfoumby.hassan.quran.domain.repository

import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio

interface SurahVerseAudioRepository {
    suspend fun getSurahVerseAudios(surah: Surah, reciterId: String, offset: Int = 0, limit: Int = Int.MAX_VALUE): List<SurahVerseAudio>

    suspend fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String)

    suspend fun downloadSurahVerseAudio(surahNumber: Int, verseNumber: Int, reciterId: String)

    suspend fun isSurahVerseAudioDownloaded(surah: Surah, reciterId: String): Boolean
}