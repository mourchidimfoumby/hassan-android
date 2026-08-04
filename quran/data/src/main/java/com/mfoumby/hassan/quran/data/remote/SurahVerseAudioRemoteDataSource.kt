package com.mfoumby.hassan.quran.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class SurahVerseAudioRemoteDataSource(private val surahVerseAudioApi: SurahVerseAudioApi) {
    private val dispatcher = Dispatchers.IO

    suspend fun downloadSurahVerseAudio(surahNumber: Int, verseNumber: Int, reciterId: String): File = withContext(dispatcher) {
        surahVerseAudioApi.downloadAudioRecitation(surahNumber, verseNumber, reciterId)
    }
}