package com.mfoumby.hassan.quran.data.local

import com.mfoumby.hassan.quran.data.mapper.toSurahVerseAudio
import com.mfoumby.hassan.quran.domain.entity.Surah
import com.mfoumby.hassan.quran.domain.entity.SurahVerseAudio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SurahVerseAudioLocalDataSource(
    private val surahVerseAudioFileStorage: SurahVerseAudioFileStorage
) {
    private val dispatcher = Dispatchers.IO

    suspend fun getSurahVerseAudios(surah: Surah, reciterId: String, offset: Int, limit: Int): List<SurahVerseAudio> =
        withContext(dispatcher) {
            surahVerseAudioFileStorage.getSurahVerseAudios(surah.number, reciterId, offset, limit).map { it.toSurahVerseAudio(surah) }
        }

    suspend fun storeSurahVerseAudio(surahNumber: Int, verseNumber: Int, reciterId: String, file: File) {
        withContext(dispatcher) {
            surahVerseAudioFileStorage.storeSurahVerseAudio(surahNumber, verseNumber, reciterId, file)
        }
    }

    suspend fun deleteSurahVerseAudios(surahNumber: Int, reciterId: String) {
        withContext(dispatcher) {
            surahVerseAudioFileStorage.deleteSurahVerseAudios(surahNumber, reciterId)
        }
    }

    suspend fun isSurahVerseAudioDownloaded(surah: Surah, reciterId: String): Boolean = withContext(dispatcher) {
        surahVerseAudioFileStorage.isSurahVerseAudioDownloaded(surah, reciterId)
    }
}